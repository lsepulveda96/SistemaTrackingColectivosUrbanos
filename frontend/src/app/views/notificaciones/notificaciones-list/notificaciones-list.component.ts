import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmComponent } from '../../misc/confirm/confirm.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MonitorService } from 'src/app/services/monitor.service';
import { NotificacionUbicacionComponent } from '../notificaciones-ubicacion/notificaciones-ubicacion.component';

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

  constructor(private servicioMonitor: MonitorService,
    private dialog: MatDialog,
    private _snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.getNotificacionesActivas();
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

      /* this.notifDS.data.forEach((item) => item.fecha = new Date(item.fecha).toLocaleDateString('en-US', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      }) );  */
     
    });
  }

  verUbicacion( notif:any) {
    const data = { 
      notif: notif 
    }
    this.dialog.open( NotificacionUbicacionComponent, { data: data, width: '600px' });
  }
}
