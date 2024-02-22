import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { LineaService } from 'src/app/services/linea.service';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-linea-edit',
  templateUrl: './linea-edit.component.html',
  styleUrls: ['./linea-edit.component.css']
})
export class LineaEditComponent implements OnInit {

  spin: boolean;
  id: any;
  linea: Linea;

  denominacionIC = new UntypedFormControl('', Validators.required);
  descripcionIC = new UntypedFormControl('');
  estadoIC = new UntypedFormControl('');

  habilitado: boolean = true;
  habilitadoIC = new UntypedFormControl(false);

  constructor(private _msg: MessageService,
    private router: Router,
    private route: ActivatedRoute,
    private servicioLinea: LineaService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get("id");
    if (!this.id)
      this.nuevaLinea();
    else
      this.editarLinea(parseInt(this.id));
  }

  nuevaLinea() {
    this.descripcionIC.setValue('');
    this.denominacionIC.setValue('');
    this.estadoIC.setValue('');
    this.habilitadoIC.setValue(true);
  }

  editarLinea(id: number) {
    this.spin = true;
    this.servicioLinea.getLinea(id).subscribe(result => {
      this.spin = false;
      if (!result.error) {
        this.linea = result.data;
        this.denominacionIC.setValue(this.linea.denominacion);
        this.descripcionIC.setValue(this.linea.descripcion);
        this.habilitado = true;
        this.habilitadoIC.setValue(this.linea.estado == 'ACTIVA');
        this.estadoIC.setValue(this.linea.estado);
      }
      else {
        this._msg.showMessage(result.mensaje, 'ERROR');
        this.router.navigate(['../..'], { relativeTo: this.route });
      }
    })
  }

  guardarLinea() {
    this.linea = {
      id: null,
      denominacion: this.denominacionIC.value,
      descripcion: this.descripcionIC.value,
      enServicio: false,
      estado: this.habilitado ? 'ACTIVA' : 'NO ACTIVA'
    }
    this.spin = true;
    this.servicioLinea.saveLinea(this.linea).subscribe(result => {
      this.spin = false;
      this._msg.showMessage(result.mensaje, result.error ? 'ERROR' : 'EXITO');
      if (!result.error)
        this.router.navigate(['../'], { relativeTo: this.route });
    });
  }

  actualizarLinea() {
    this.linea.denominacion = this.denominacionIC.value;
    this.linea.descripcion = this.descripcionIC.value;
    this.linea.estado = this.habilitadoIC.value ? 'ACTIVA' : 'NO ACTIVA';
    this.spin = true;
    this.servicioLinea.updateLinea(this.linea).subscribe(result => {
      this.spin = false;
      this._msg.showMessage(result.mensaje, result.error ? 'ERROR' : 'EXITO');
      if (!result.error)
        this.router.navigate(['../..'], { relativeTo: this.route });
    });
  }

}
