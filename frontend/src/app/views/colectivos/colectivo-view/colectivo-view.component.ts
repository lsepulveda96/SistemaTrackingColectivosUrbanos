import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { Colectivo } from 'src/app/data/colectivo';
import { ColectivoService } from 'src/app/services/colectivo.service';

@Component({
  selector: 'app-colectivo-view',
  templateUrl: './colectivo-view.component.html',
  styleUrls: ['./colectivo-view.component.css']
})
export class ColectivoViewComponent implements OnInit {

  waiting: boolean;
  opening: boolean;
  colectivo: Colectivo;
  url: any;

  constructor(private servicioColectivo: ColectivoService,
    private _matsnack: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router,
    private readonly sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this._matsnack.open('No se especifico id de colectivo', 'Error', {
        duration: 3500,
        verticalPosition: 'top',
        horizontalPosition: 'right',
        panelClass: ['red-snackbar']
      });
      this.router.navigate(['../..']);
      return;
    }
    this.getColectivo(parseInt(id));

  }

  getColectivo(id: number) {
    this.waiting = true;
    this.servicioColectivo.getColectivo(id)
      .subscribe(result => {
        this.waiting = false;
        if (result.error) {
          this._matsnack.open('No se especifico id de colectivo', 'Error', {
            duration: 3500,
            verticalPosition: 'top',
            horizontalPosition: 'right',
            panelClass: ['red-snackbar']
          });
          this.router.navigate(['../..']);
          return;
        }
        this.colectivo = result.data;
        if (this.colectivo.imgpath) {
          this.servicioColectivo
            .downloadImagen(this.colectivo.imgpath)
            .subscribe(img => {
              const blob = new Blob([img], { type: img.type });
              this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
                window.URL.createObjectURL(blob)
              );
            })
        }
      })
  }

  getClass(name: string) {
    const ext = name.split('.').pop();
    if (ext == 'pdf')
      return 'bi bi-file-pdf-fill text-danger';
    else if (ext == 'jpg' || ext == 'jpeg' || ext == 'png')
      return 'bi bi-file-image-fill text-primary';
    return 'bi bi-file-earmark-fill text-secondary';
  }

  openDocument(doc: any) {
    this.opening = true;
    this.servicioColectivo.downloadDoc(doc.pathfile).subscribe(data => {
      this.opening = false;
      if (!data.error) {
        const blob = new Blob([data]);
        window.open(window.URL.createObjectURL(blob));
      }
    });
  }

  getColorVencimiento(fecha: Date): string {
    if (!fecha) return 'black';
    const fechaComp = moment();
    const fechaVenc = moment(fecha);
    if (fechaVenc.isSameOrBefore(fechaComp))
      return 'red';

    fechaComp.add(1, 'month');
    if (fechaVenc.isSameOrBefore(fechaComp))
      return 'orange';

    return 'black';
  }

  isVencido(fecha: Date): boolean {
    if (!fecha) return false;
    const fechaComp = moment(); // hoy
    const fechaVenc = moment(fecha);
    if (fechaVenc.isSameOrBefore(fechaComp))
      return true;
    return false;
  }

  isProximoVencer( fecha:Date): boolean {
    if (!fecha) return false;
    const fechaComp = moment().add(1,'month'); // un mes adelante.
    const fechaVenc = moment(fecha);
    if (fechaVenc.isSameOrBefore(fechaComp))
      return true;
    return false;
  }
}
