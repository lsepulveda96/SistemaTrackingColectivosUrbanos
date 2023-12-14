import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { ConfirmComponent } from '../../misc/confirm/confirm.component';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-paradas-list',
  templateUrl: './paradas-list.component.html',
  styleUrls: ['./paradas-list.component.css']
})
export class ParadasListComponent implements OnInit, AfterViewInit {

  spin: boolean = false;
  paradas: Parada[] = [];
  paradasDS: MatTableDataSource<Parada> = new MatTableDataSource<Parada>([]);
  
  isadmin: boolean = false;

  @ViewChild('pag') paginator: MatPaginator;

  constructor( 
    private serviceParada: ParadaService,
    private tokenService: TokenStorageService,
    private _snackbar: MatSnackBar,
    private dialog: MatDialog, ) { 
      this.isadmin = this.tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    this.getParadas();
  }

  ngAfterViewInit(): void {
      this.paradasDS.paginator = this.paginator;
  }
  
  getParadas() {
    this.spin = true;
    this.serviceParada.getParadas()
      .subscribe( result => {
        this.spin = false;
        this.paradas = result.data;
        this.paradasDS.data = this.paradas;
      });
  }

  bajaParada(parada: Parada) {
    if (parada.codigo) {
      const ref = this.dialog.open(ConfirmComponent, { data: 
        { titulo: 'Parada', mensaje: 'Confirma dar de baja parada ' + parada.codigo + ' ' + parada.direccion + '?' } 
      });
      ref.afterClosed().subscribe(aceptar => {
        if (aceptar) {
          this.spin = true;
          this.serviceParada.enableDisableParada( parada.codigo, true ).subscribe(result => {
            this.spin = false;
            this._snackbar.open( result.mensaje,'',
              {
                duration: 4500,
                verticalPosition: 'top', // 'top' | 'bottom'
                horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
                panelClass: result.error ? ['red-snackbar']:['blue-snackbar']
              });
            if (!result.error)
              this.getParadas();
          });
        }
      });
    }
  }

  activarParada( parada: Parada ) {
    if (parada.codigo) {
      this.spin = true;
      this.serviceParada.enableDisableParada( parada.codigo, false ).subscribe( result => {
        this.spin = false;
        this._snackbar.open( result.mensaje, '', {
          duration: 4500, 
          verticalPosition: 'top', horizontalPosition:'end',
          panelClass: result.error ? ['red-snackbar']:['blue-snackbar']
        });
        if (!result.error) 
          this.getParadas();
      });
    }
  }
}
