import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Colectivo } from 'src/app/data/colectivo';
import { ColectivoService } from 'src/app/services/colectivo.service';

@Component({
  selector: 'app-colectivo-view',
  templateUrl: './colectivo-view.component.html',
  styleUrls: ['./colectivo-view.component.css']
})
export class ColectivoViewComponent implements OnInit {

  waiting: boolean;
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
          this.waiting = true;
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

}
