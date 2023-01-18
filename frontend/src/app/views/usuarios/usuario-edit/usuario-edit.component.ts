import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Usuario } from 'src/app/data/usuario';
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

  usuarioIC = new UntypedFormControl('',[Validators.required, Validators.pattern('^[a-zA-Z0-9]*$'),Validators.minLength(5)] );
  passwdIC = new UntypedFormControl('',[Validators.required, Validators.minLength(8), Validators.maxLength(15)]);
  passwd2IC = new UntypedFormControl('',[Validators.required, Validators.minLength(8), Validators.maxLength(15)] );
  nombreIC = new UntypedFormControl( '',Validators.required );
  apellidoIC = new UntypedFormControl( '',Validators.required );
  dniIC = new UntypedFormControl('');
  direccionIC = new UntypedFormControl('');
  telefonoIC = new UntypedFormControl('') ;
  emailIC = new UntypedFormControl('', Validators.email );
  superusrIC = new UntypedFormControl(false);

  constructor( private servicioUsuario: UsuarioService,
              private _snackbar: MatSnackBar,
              private route: ActivatedRoute,
              private router: Router ) { }

  ngOnInit(): void {
    this.id = parseInt( this.route.snapshot.paramMap.get('id'));
    if (this.id)
      this.editarUsuario( this.id );
    else 
      this.nuevoUsuario();
  }

  nuevoUsuario() {
    this.usuarioIC.setValue( '' );
    this.nombreIC.setValue( '' );
    this.apellidoIC.setValue( '' );
    this.dniIC.setValue( '' );
    this.direccionIC.setValue( '' );
    this.telefonoIC.setValue( '' );
    this.emailIC.setValue( '' );
    this.superusrIC.setValue( false );
  }

  editarUsuario( id: number ) {
    this.waiting = true;
    this.servicioUsuario.getUsuario( id )
      .subscribe( result => {
        console.log("editar usuario: ", result );
        this.waiting = false;
        if (result.error) {
          this._snackbar.open( result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'bottom', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar']
          });
          this.router.navigate( ['../..'], { relativeTo: this.route });
          return;
        }
        this.usuario = result.data;
        this.usuarioIC.setValue( this.usuario.usuario );
        this.nombreIC.setValue( this.usuario.nombre );
        this.apellidoIC.setValue( this.usuario.apellido );
        this.dniIC.setValue( this.usuario.dni );
        this.direccionIC.setValue( this.usuario.direccion );
        this.telefonoIC.setValue( this.usuario.telefono );
        this.emailIC.setValue( this.usuario.email );
        this.superusrIC.setValue( this.usuario.superusuario );
      })
  }

  guardarUsuario() {
    if (this.passwdIC.value != this.passwd2IC.value)  {
      this._snackbar.open( 'Las contraseñas no coinciden','', {
        duration: 4500,
        verticalPosition: 'bottom',
        horizontalPosition: 'end',
        panelClass:  ['red-snackbar']      
      });
      return;
    }
      
    this.usuario = {
      id: null,
      usuario: this.usuarioIC.value,
      passwd: this.passwdIC.value,
      nombre: this.nombreIC.value,
      apellido: this.apellidoIC.value,
      dni: this.dniIC.value,
      direccion: this.direccionIC.value,
      telefono: this.telefonoIC.value,
      email: this.emailIC.value,
      superusuario: this.superusrIC.value,
      estado: 'HABILITADO'
    }
    this.waiting = true;
    this.servicioUsuario.saveUsuario( this.usuario )
      .subscribe( result => {
        this.waiting = false;
        this._snackbar.open( result.mensaje, '', {
          duration: 4500,
          verticalPosition: result.error ? 'bottom':'top',
          horizontalPosition: 'end',
          panelClass: result.error ? ['red-snackbar']: ['blue-snackbar']
        });
        if (!result.error)
          this.router.navigate( ['../'], {relativeTo: this.route });
      });
  }

  actualizarUsuario() {
    this.waiting = true;
    this.usuario.usuario = this.usuarioIC.value;
    this.usuario.nombre = this.nombreIC.value;
    this.usuario.apellido = this.apellidoIC.value;
    this.usuario.dni = this.dniIC.value;
    this.usuario.direccion = this.direccionIC.value;
    this.usuario.telefono = this.telefonoIC.value;
    this.usuario.email = this.emailIC.value;
    this.usuario.superusuario = this.superusrIC.value;
    this.usuario.passwd = '';

    console.log("*** Actualizar Usuario: ", this.usuario );
  
    this.servicioUsuario.updateUsuario( this.usuario )
      .subscribe( result => {
        this.waiting = false;
        this._snackbar.open( result.mensaje, '', {
          duration: 4500,
          verticalPosition: result.error ? 'bottom':'top',
          horizontalPosition: 'end',
          panelClass: result.error ? ['red-snackbar']: ['blue-snackbar']
        });
        if (!result.error)
          this.router.navigate( ['../..'], {relativeTo: this.route });
      });
  }

  deshabilitarUsuario() {

  }

}
