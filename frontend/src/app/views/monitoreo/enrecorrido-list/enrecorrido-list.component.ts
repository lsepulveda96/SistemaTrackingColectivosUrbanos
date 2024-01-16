import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorService } from 'src/app/services/monitor.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmComponent } from '../../misc/confirm/confirm.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-enrecorrido-list',
  templateUrl: './enrecorrido-list.component.html',
  styleUrls: ['./enrecorrido-list.component.css']
})
export class EnrecorridoListComponent implements OnInit, AfterViewInit {

  waiting: boolean;
  transito: any[] = [];
  transitoDS: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  @ViewChild('pag') pag: MatPaginator;

  constructor(private servicioMonitor: MonitorService,
    private dialog: MatDialog,
    private _snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.getUnidadesEnRecorrido();
  }

  ngAfterViewInit(): void {
    this.transitoDS.paginator = this.pag;
  }

  getUnidadesEnRecorrido() {
    this.waiting = true;
    this.servicioMonitor.getUnidadesTransito().
      subscribe(result => {
        this.waiting = false;
        this.transito = result.data;
        this.transitoDS.data = this.transito;
      });
  }


  detenerColRec(idUnidad: number, idLinea: number) {
    console.log("cole rec que se va a detener" + idUnidad)
    const ref = this.dialog.open(ConfirmComponent, {
      data:
        { titulo: 'Detener servicio', mensaje: 'Confirma detener unidad ?' }
    });
    ref.afterClosed().subscribe(aceptar => {
      if (aceptar) {
        this.waiting = true;
        this.servicioMonitor.detenerColRec(idUnidad, idLinea, false).subscribe(result => {
          this.waiting = false;
          this._snackbar.open(result.mensaje, '',
            {
              duration: 4500,
              verticalPosition: 'top', // 'top' | 'bottom'
              horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
              panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar']
            });
          if (!result.error)
            this.getUnidadesEnRecorrido();
        });
      }
    });
  }
}
