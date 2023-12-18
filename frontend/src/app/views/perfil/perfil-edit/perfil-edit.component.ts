import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Form, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Usuario } from 'src/app/data/usuario';
import { MessageService } from 'src/app/services/message.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-perfil-edit',
  templateUrl: './perfil-edit.component.html',
  styleUrls: ['./perfil-edit.component.css']
})
export class PerfilEditComponent implements OnInit {

  waiting: boolean;
  usuario: Usuario;

  nombreIC = new FormControl('', Validators.required);
  apellidoIC = new FormControl('', Validators.required);
  emailIC = new FormControl('');
  telefonoIC = new FormControl('');
  direccionIC = new FormControl('');
  dniIC = new FormControl('');

  super: boolean;

  panelCambioPass: boolean = false;
  actualPass = new FormControl('', Validators.required);
  newPass1 = new FormControl('', [Validators.required, Validators.minLength(8)]);
  newPass2 = new FormControl('', [Validators.required, Validators.minLength(8)]);

  constructor(
    private serviceUsuario: UsuarioService,
    private serviceMsg: MessageService,
    private _snackbar: MatSnackBar,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.getPerfilUsuario(parseInt(id));
  }

  getPerfilUsuario(id: number) {
    this.waiting = true;
    this.serviceUsuario.getUsuario(id).subscribe(result => {
      this.waiting = false;
      if (result.error) {
        this._snackbar.open(result.mensaje, "", {
          duration: 4500,
          verticalPosition: "top", // 'top' | 'bottom'
          horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: ["red-snackbar"],
        });
        this.cerrar();
      }
      this.usuario = result.data;
      console.log("get usuario: ", this.usuario);
      this.apellidoIC.setValue(this.usuario.apellido);
      this.nombreIC.setValue(this.usuario.nombre);
      this.dniIC.setValue(this.usuario.dni);
      this.direccionIC.setValue(this.usuario.direccion);
      this.emailIC.setValue(this.usuario.email);
      this.telefonoIC.setValue(this.usuario.telefono);
      const isSuper = result.data.roles?.find((r: any) => r == 'ROLE_ADMIN');
      this.super = isSuper != null;
    })
  }

  modoCambiarPass() {
    this.panelCambioPass = !this.panelCambioPass;
    this.actualPass.setValue('');
    this.newPass1.setValue('');
    this.newPass2.setValue('');
  }

  cambiarPass() {
    this.waiting = true;
    if (this.newPass1.value != this.newPass2.value) {
      this.serviceMsg.showMessage('La nueva contraseña no coincide', 'ERROR');
      this.waiting = false;
      return;
    }
    this.serviceUsuario.changePasswd(this.usuario.id, this.actualPass.value, this.newPass1.value)
      .subscribe(
        result => {
          console.log("result; ", result);
        },
        err => {
          console.log("error: ", err);
        });
  }

  actualizar() {
    this.waiting = true;
    this.serviceUsuario.updateUsuario(this.usuario.id, this.super, this.apellidoIC.value,
      this.nombreIC.value, this.emailIC.value, this.dniIC.value, this.direccionIC.value, this.telefonoIC.value)
      .subscribe(result => {
        this.waiting = false;
        this.serviceMsg.showMessage(result.error ? result.mensaje : 'Perfil actualizado', result.error ? 'ERROR' : 'EXITO')
        if (!result.error)
          this.location.back();
      });
  }

  cerrar() {
    this.location.back();
  }
}
