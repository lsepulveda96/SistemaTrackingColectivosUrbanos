import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MonitorService } from 'src/app/services/monitor.service';
import { NotificacionUbicacionComponent } from '../notificaciones-ubicacion/notificaciones-ubicacion.component';
import { ColectivoService } from 'src/app/services/colectivo.service';
import * as moment from 'moment';

@Component({
  selector: 'app-notificaciones-list',
  templateUrl: './notificaciones-list.component.html',
  styleUrls: ['./notificaciones-list.component.css'],
})
export class NotificacionesListComponent implements OnInit, AfterViewInit {

  spin: boolean = false;
  waiting: boolean;
  transito: any[];
  notifDS: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  @ViewChild('pag') pag: MatPaginator;

  vencimientos: any[];
  viewVencimientos: boolean;

  constructor(
    private servicioMonitor: MonitorService,
    private servicioColectivo: ColectivoService,
    private dialog: MatDialog,
    private _snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.getNotificacionesActivas();
    this.getVencimientos();
    this.viewDocsVencimientos();
  }

  ngAfterViewInit(): void {
    this.notifDS.paginator = this.pag;
  }

  getNotificacionesActivas() {
    console.log("notificaciones activas: ");
    this.waiting = true;
    this.servicioMonitor.getNotificacionesActivas().subscribe(result => {
      this.waiting = false;
      this.notifDS.data = result.data
    });
  }

  verUbicacion(notif: any) {
    const data = {
      notif: notif
    }
    this.dialog.open(NotificacionUbicacionComponent, { data: data, width: '600px' });
  }

  getVencimientos() {
    this.waiting = true;
    this.servicioColectivo.getDocumentosVencimientos().subscribe(result => {
      this.waiting = false;
      console.log("documentos vencimientos result: ", result);
      const hoy = moment();
      this.vencimientos = result.data.map( (col:any) => {
        for (let doc of col.documentos) {
          const venc = moment(doc.vencimiento);
          doc.vencido = venc.isSameOrBefore(hoy);
        }
        return col;
      });
    });
  }

  viewNotifTransito() {
    this.viewVencimientos = false;
  }

  viewDocsVencimientos() {
    this.viewVencimientos = true;
  }
}
