import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Usuario } from 'src/app/data/usuario';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-usuarios-list',
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent implements OnInit {

  waiting: boolean = false;
  usuarios: Usuario[] = [];
  usuariosDS: MatTableDataSource<Usuario> = new MatTableDataSource<Usuario>([]);

  isadmin: boolean;

  constructor(
    private serviceUsuario: UsuarioService,
    private tokenService: TokenStorageService) {
    this.isadmin = this.tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    if (this.isadmin)
      this.getUsuarios();
  }

  getUsuarios() {
    this.waiting = true;
    this.serviceUsuario.getUsuarios()
      .subscribe(result => {
        this.waiting = false;
        this.usuarios = result.data;
        this.usuariosDS.data = this.usuarios;
      });
  }

}
