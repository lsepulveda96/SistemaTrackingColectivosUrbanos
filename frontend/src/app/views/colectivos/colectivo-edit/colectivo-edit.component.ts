import { ThrowStmt } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, UntypedFormControl, Validators } from '@angular/forms';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Colectivo } from 'src/app/data/colectivo';
import { ColectivoService } from 'src/app/services/colectivo.service';
import { ConfirmComponent } from '../../misc/confirm/confirm.component';

@Component({
  selector: 'app-colectivo-edit',
  templateUrl: './colectivo-edit.component.html',
  styleUrls: ['./colectivo-edit.component.css'],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'es-AR' },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS }
  ]
})
export class ColectivoEditComponent implements OnInit {

  spin: boolean = true;
  id: any;
  colectivo: Colectivo | undefined;

  unidadIC = new FormControl('', Validators.required);
  patenteIC = new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z]{2}[0-9]{3}[a-zA-Z]{2}$|^[a-zA-Z]{3}[0-9]{3}$')]);
  marcaIC = new FormControl('', Validators.required);
  modeloIC = new FormControl('');
  anioIC = new FormControl('', Validators.pattern('^[12]{1}[09]{1}[0-9]{2}$')); // 19xx o 20xx
  capacidadIC = new FormControl('', Validators.pattern('^[1-9][0-9]?$')); // numero entre 1 y 100
  compraIC = new FormControl('');

  estados: string[] = ['HABILITADO', 'NO HABILITADO'];
  estadoIC = new FormControl('');

  constructor(private router: Router,
    private route: ActivatedRoute,
    private serviceColectivo: ColectivoService,
    private dialog: MatDialog,
    private _snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id)
      this.editarColectivo(parseInt(this.id));
    else
      this.nuevoColectivo();
  }

  nuevoColectivo() {
    this.spin = true;
    this.unidadIC.setValue('');
    this.patenteIC.setValue('')
    this.marcaIC.setValue('');
    this.modeloIC.setValue('')
    this.anioIC.setValue('')
    this.capacidadIC.setValue(null);
    this.compraIC.setValue(null);
    this.estadoIC.setValue(this.estados[0]);
    this.spin = false;
  }

  editarColectivo(id: number) {
    this.spin = true;
    this.serviceColectivo.getColectivo(id)
      .subscribe(result => {
        if (result.error) {
          this._snackbar.open('No se pudo recuperar Colectivo ' + id);
          return;
        }
        else {
          this.colectivo = result.data;
          this.unidadIC.setValue(this.colectivo.unidad);
          this.patenteIC.setValue(this.colectivo.patente)
          this.marcaIC.setValue(this.colectivo.marca);
          this.modeloIC.setValue(this.colectivo.modelo)
          this.anioIC.setValue(this.colectivo.anio)
          this.capacidadIC.setValue(this.colectivo.capacidad);
          this.compraIC.setValue(new Date(this.colectivo.fechaCompra));
          this.estadoIC.setValue(this.colectivo.estado);
        }
        this.spin = false;
      })
  }



  guardarColectivo() {
    this.cargarValores();
    this.spin = true;
    this.serviceColectivo.saveColectivo(this.colectivo)
      .subscribe(result => {
        this._snackbar.open(result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
        });
        if (!result.error)
          this.router.navigate(['../'], { relativeTo: this.route });
        this.spin = false;
      });
  }

  actualizarColectivo() {
    this.cargarValores();
    this.spin = true;
    this.serviceColectivo.updateColectivo(this.colectivo)
      .subscribe(result => {
        this._snackbar.open(result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
        });
        if (!result.error)
          this.router.navigate(['../..'], { relativeTo: this.route });
        this.spin = false;
      });
  }

  cargarValores() {
    this.colectivo = {
      id: this.id,
      unidad: this.unidadIC.value.toUpperCase(),
      marca: this.marcaIC.value.toUpperCase(),
      modelo: this.modeloIC.value.toUpperCase(),
      patente: this.patenteIC.value.toUpperCase(),
      anio: this.anioIC.value,
      capacidad: this.capacidadIC.value,
      fechaCompra: this.compraIC.value,
      estado: this.estadoIC.value,
      enCirculacion: false,
      fechaBaja: null,
    }
  }

  bajaColectivo() {
    const ref = this.dialog.open(ConfirmComponent, { data: { titulo: 'Baja de colectivo', mensaje: 'Confirma dar de baja unidad ' + this.colectivo.unidad + '?' } });
    ref.afterClosed().subscribe(aceptar => {
      if (aceptar) {
        this.spin = true;
        this.serviceColectivo.disableColectivo(this.colectivo.id).subscribe(resultOk => {
          this.spin = false;
          this._snackbar.open(
            resultOk ? 'Colectivo ' + this.colectivo.unidad + ' se dio de baja' : 'No se pudo dar de baja Colectivo ' + this.colectivo.unidad,
            '',
            {
              duration: 4500,
              verticalPosition: 'top', // 'top' | 'bottom'
              horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
              panelClass: resultOk ? ['blue-snackbar'] : ['red-snackbar'],
            });
          if (resultOk)
            this.router.navigate(['../..'], { relativeTo: this.route });
        });
      }
    });
  }
}
