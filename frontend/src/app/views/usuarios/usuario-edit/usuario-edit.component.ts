import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Usuario } from 'src/app/data/usuario';
import { AuthService } from 'src/app/services/auth.service';
import { MessageService } from 'src/app/services/message.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-usuario-edit',
  templateUrl: './usuario-edit.component.html',
  styleUrls: ['./usuario-edit.component.css']
})
export class UsuarioEditComponent implements OnInit {

  waiting: boolean;
  id: number;
  usuario: Usuario;

  usuarioIC = new UntypedFormControl('', [Validators.required, Validators.pattern('^[a-zA-Z0-9]*$'), Validators.minLength(5)]);
  passwdIC = new UntypedFormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(15)]);
  passwd2IC = new UntypedFormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(15)]);
  nombreIC = new UntypedFormControl('', Validators.required);
  apellidoIC = new UntypedFormControl('', Validators.required);
  dniIC = new UntypedFormControl('');
  direccionIC = new UntypedFormControl('');
  telefonoIC = new UntypedFormControl('');
  emailIC = new UntypedFormControl('', Validators.email);
  superusrIC = new UntypedFormControl(false);

  constructor(private servicioUsuario: UsuarioService,
    private servicioAuth: AuthService,
    private serviceMsg: MessageService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    if (this.id)
      this.editarUsuario(this.id);
    else
      this.nuevoUsuario();
  }

  nuevoUsuario() {
    this.usuarioIC.setValue('');
    this.nombreIC.setValue('');
    this.apellidoIC.setValue('');
    this.dniIC.setValue('');
    this.direccionIC.setValue('');
    this.telefonoIC.setValue('');
    this.emailIC.setValue('');
    this.superusrIC.setValue(false);
  }

  editarUsuario(id: number) {
    this.waiting = true;
    this.servicioUsuario.getUsuario(id)
      .subscribe(result => {
        this.waiting = false;
        if (result.error) {
          this.serviceMsg.showMessage(result.mensaje, 'ERROR ');
          this.router.navigate(['../..'], { relativeTo: this.route });
          return;
        }
        this.usuario = result.data;
        this.usuarioIC.setValue(this.usuario.usuario);
        this.nombreIC.setValue(this.usuario.nombre);
        this.apellidoIC.setValue(this.usuario.apellido);
        this.dniIC.setValue(this.usuario.dni);
        this.direccionIC.setValue(this.usuario.direccion);
        this.telefonoIC.setValue(this.usuario.telefono);
        this.emailIC.setValue(this.usuario.email);
        const superusr = result.data.roles?.find((r: any) => r == 'ROLE_ADMIN');
        this.superusrIC.setValue(superusr ? true : false);
      })
  }

  registrarUsuario() {
    if (this.passwdIC.value != this.passwd2IC.value) {
      this.serviceMsg.showMessage('Las contraseñas no coinciden', 'ERROR');
      return;
    }
    this.waiting = true;
    this.servicioAuth.register(this.usuarioIC.value, this.passwdIC.value, this.superusrIC.value,
      this.apellidoIC.value, this.nombreIC.value, this.emailIC.value,
      this.dniIC.value, this.direccionIC.value, this.telefonoIC.value)
      .subscribe(result => {
        this.waiting = false;
        this.serviceMsg.showMessage(result.message, 'OK');
        this.router.navigate(['usuarios']);
      }, err => {
        this.waiting = false;
        this.serviceMsg.showMessage(err.error.message, 'ERROR');
      });
  }

  actualizarUsuario() {
    this.waiting = true;
    this.servicioUsuario.updateUsuario(this.usuario.id, this.superusrIC.value, this.apellidoIC.value,
      this.nombreIC.value, this.emailIC.value, this.dniIC.value, this.direccionIC.value, this.telefonoIC.value)
      .subscribe(result => {
        this.waiting = false;
        this.serviceMsg.showMessage(result.mensaje, result.error ? 'ERROR' : 'EXITO')
        if (!result.error)
          this.router.navigate(['../..'], { relativeTo: this.route });
      });
  }

}
