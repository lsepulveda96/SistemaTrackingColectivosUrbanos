import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Usuario } from 'src/app/data/usuario';
import { MessageService } from 'src/app/services/message.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import { ConfirmComponent } from '../../misc/confirm/confirm.component';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-usuarios-list',
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css']
})
export class UsuariosListComponent implements OnInit, AfterViewInit {

  waiting: boolean = false;
  usuarios: Usuario[] = [];
  usuariosDS: MatTableDataSource<Usuario> = new MatTableDataSource<Usuario>([]);

  isadmin: boolean;

  @ViewChild('pag') paginator: MatPaginator;

  constructor(
    private serviceUsuario: UsuarioService,
    private serviceMsg: MessageService,
    private dialog: MatDialog,
    private tokenService: TokenStorageService) {
    this.isadmin = this.tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    if (this.isadmin)
      this.getUsuarios();
  }

  ngAfterViewInit(): void {
    this.usuariosDS.paginator = this.paginator;
  }

  getUsuarios() {
    this.waiting = true;
    this.serviceUsuario.getUsuarios()
      .subscribe(result => {
        this.waiting = false;
        this.usuarios = result.data.filter((usr: Usuario) =>
          usr.id != this.tokenService.getUser().id && usr.usuario != 'sysadmin'
        );
        this.usuariosDS.data = this.usuarios;
      });
  }

  bajaUsuario(id: number, usr: string) {
    const data = { titulo: 'Desactivar usuario', mensaje: 'Confirma desactivar usuario ' + usr + '?' };
    const refDialog = this.dialog.open(ConfirmComponent, { data: data });
    refDialog.afterClosed().subscribe(aceptar => {
      if (aceptar) {
        this.waiting = true;
        this.serviceUsuario.deactivateUsuario(id).subscribe(result => {
          this.serviceMsg.showMessage(result.message, 'OK');
          this.getUsuarios();
        }, err => {
          this.serviceMsg.showMessage(err.error.message, 'ERROR');
        }, () => {
          this.waiting = false;
        });
      }
    });
  }

  activarUsuario(id: number, usr: String) {
    const data = { titulo: 'Activacion usuario', mensaje: 'Confirma activar usuario ' + usr + '?' };
    const dialog = this.dialog.open(ConfirmComponent, { data: data });
    dialog.afterClosed().subscribe(response => {
      if (response) {
        this.waiting = true;
        this.serviceUsuario.activateUsuario(id).subscribe(result => {
          console.log("usuario activado: ", result);
          this.serviceMsg.showMessage(result.message, 'OK');
          this.getUsuarios();
        }, err => {
          console.log("ERROR activando usuario : ", err);
          this.serviceMsg.showMessage(err.error.message, 'ERROR');
        }, () => {
          this.waiting = false;
        });

      }
    })
  }
}
