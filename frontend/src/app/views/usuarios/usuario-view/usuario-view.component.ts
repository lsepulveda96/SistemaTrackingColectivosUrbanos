import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Usuario } from 'src/app/data/usuario';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-usuario-view',
  templateUrl: './usuario-view.component.html',
  styleUrls: ['./usuario-view.component.css']
})
export class UsuarioViewComponent implements OnInit {

  waiting: boolean;
  usuario: Usuario;
  issuper: boolean;

  constructor( private servicioUsuario: UsuarioService,
              private _snackbar: MatSnackBar,
              private route: ActivatedRoute,
              private router: Router ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.getUsuario( parseInt(id));
  }

  getUsuario( id: number ) {
    this.waiting  =true;
    this.servicioUsuario.getUsuario( id )
      .subscribe( result => {
        this.waiting = false;
        if (result.error) {
          this._snackbar.open( result.mensaje, 'Error', {
            duration: 3500,
            verticalPosition: 'top',
            horizontalPosition: 'right',
            panelClass: ['snack-red']
          });
          this.router.navigate( ['../..'],{ relativeTo: this.route });
        }
        this.usuario = result.data;
        this.issuper = result.data.roles?.find( (r: string) => r == 'ROLE_ADMIN');
      });
  }

}
