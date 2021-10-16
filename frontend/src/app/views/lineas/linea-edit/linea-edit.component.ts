import { ResourceLoader } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { LineaService } from 'src/app/services/linea.service';

@Component({
  selector: 'app-linea-edit',
  templateUrl: './linea-edit.component.html',
  styleUrls: ['./linea-edit.component.css']
})
export class LineaEditComponent implements OnInit {

  spin: boolean;
  id: any;
  linea: Linea;

  denominacionIC = new FormControl('', Validators.required );
  descripcionIC = new FormControl('');
  estadoIC = new FormControl('');

  habilitado: boolean = true;

  constructor( private _snackbar: MatSnackBar, 
              private router: Router,
              private route: ActivatedRoute,
              private servicioLinea: LineaService ) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get("id");
    if (!this.id)
      this.nuevaLinea();
    else
      this.editarLinea( parseInt( this.id ) );
  }

  nuevaLinea() {
    this.descripcionIC.setValue('');
    this.denominacionIC.setValue('');
    this.estadoIC.setValue('');
  }

  editarLinea( id: number ) {
    this.spin = true;
    this.servicioLinea.getLinea( id )
      .subscribe( result => {
        if (!result.error) {
          this.linea = result.data;
          this.denominacionIC.setValue( this.linea.denominacion );
          this.descripcionIC.setValue( this.linea.descripcion );
          this.habilitado = true;
          this.estadoIC.setValue( this.linea.estado );
        }
        else {
          this._snackbar.open( result.mensaje,'', {
            duration: 4500,
            horizontalPosition: 'end',
            verticalPosition: 'top',
            panelClass: ['red-snackbar']
          });
          this.router.navigate( ['../..'], { relativeTo: this.route });
        }
        this.spin = false;
      })
  }

  guardarLinea() {
    this.linea = {
      id: null,
      denominacion: this.denominacionIC.value,
      descripcion: this.descripcionIC.value,
      enServicio: false,
      estado: this.habilitado ? 'ACTIVA':'NO ACTIVA'
    }
    this.spin = true;
    this.servicioLinea.saveLinea( this.linea )
      .subscribe( result => {
        this._snackbar.open( result.mensaje,'', {
          duration: 4500,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: result.error ? ['red-snackbar']:['blue-snackbar']
        });
        if (!result.error) {
          this.router.navigate( ['../'], {relativeTo : this.route});
        }
      });
  }

  actualizarLinea() {
    this.linea.denominacion = this.denominacionIC.value;
    this.linea.descripcion = this.descripcionIC.value;
    this.linea.estado = this.habilitado ? 'ACTIVA':'NO ACTIVA';
    this.spin = true;
    this.servicioLinea.updateLinea( this.linea )
      .subscribe( result => {
        this._snackbar.open( result.mensaje, '', {
          duration: 4000,
          horizontalPosition: 'end',
          verticalPosition:'top',
          panelClass: result.error ? ['red-snackbar']:['blue-snackbar']
          
        });
        if (!result.error) {
          this.router.navigate( ['../..'], { relativeTo: this.route });
        }
      })
  }

}
