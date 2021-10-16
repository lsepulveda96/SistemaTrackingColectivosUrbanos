import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Usuario } from 'src/app/data/usuario';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-usuarios-list',
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent implements OnInit {

  waiting: boolean = false;
  usuarios: Usuario[] = [];
  usuariosDS: MatTableDataSource<Usuario> = new MatTableDataSource<Usuario>( []);
  
  constructor( private serviceUsuario: UsuarioService ) { }

  ngOnInit(): void {
    this.getUsuarios();
  }

  getUsuarios() {
    this.waiting = true;
    this.serviceUsuario.getUsuarios()
      .subscribe( result => {
          this.usuarios = result.data;
          this.usuariosDS.data = this.usuarios;
          this.waiting = false;
      });
  }

}
