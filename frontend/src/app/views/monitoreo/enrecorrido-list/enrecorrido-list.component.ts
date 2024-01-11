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

  spin: boolean = false;
  waiting: boolean;
  transito: any[];
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
    console.log("Unidades en recorrido: ");
    this.waiting = true;
    this.servicioMonitor.getUnidadesTransito().subscribe(result => {
      this.waiting = false;
      this.transitoDS.data = result.data;

      const list = document.getElementById("list");

      for (let i = 0; i < result.data.length; i++) {
        console.log("data: " + JSON.stringify(result.data[i]));
      }
    });


      

    /*this.transito = [
      { colectivo: {id: 1, unidad: 'colectivo 3', }, linea: {id: 2, denominacion:'linea 2', }, recorrido: {id: 1, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 4', }, linea: {id: 2, denominacion:'linea 2', }, recorrido: {id: 2, denominacion: 'REGRESO'} },
      { colectivo: {id: 1, unidad: 'colectivo 1', }, linea: {id: 2, denominacion:'linea 1', }, recorrido: {id: 2, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 5', }, linea: {id: 2, denominacion:'linea 5', }, recorrido: {id: 2, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 6', }, linea: {id: 2, denominacion:'linea 3', }, recorrido: {id: 2, denominacion: 'REGRESO'} },
      { colectivo: {id: 1, unidad: 'colectivo 22',}, linea: {id: 2, denominacion:'linea 1',}, recorrido: {id: 2,  denominacion: 'REGRESO'} }
    ]; */
    //this.transitoDS.data = this.transito;
  }


  detenerColRec(idUnidad: number, idLinea: number){
    
    console.log("cole rec que se va a detener"+ idUnidad)
      const ref = this.dialog.open(ConfirmComponent, {
        data:
          { titulo: 'Detener servicio', mensaje: 'Confirma detener unidad ?' }
      });
      ref.afterClosed().subscribe(aceptar => {
        if (aceptar) {
          this.spin = true;
          this.servicioMonitor.detenerColRec(idUnidad,idLinea, false).subscribe(result => {
            this.spin = false;
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
