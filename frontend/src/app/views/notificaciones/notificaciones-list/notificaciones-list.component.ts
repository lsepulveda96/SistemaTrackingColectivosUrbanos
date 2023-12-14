import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-notificaciones-list',
  templateUrl: './notificaciones-list.component.html',
  styleUrls: ['./notificaciones-list.component.css']
})
export class NotificacionesListComponent implements OnInit, AfterViewInit {

  spin: boolean;
  notificacionesDS: MatTableDataSource<any> = new MatTableDataSource<any>([]);

  @ViewChild('pag') paginator: MatPaginator;

  constructor(
  ) { }

  ngOnInit(): void {
    this.spin = true;
    this.getNotificaciones();
    this.spin = false;
  }

  ngAfterViewInit(): void {
      this.notificacionesDS.paginator = this.paginator;
  }

  getNotificaciones() {
    const data = [
      {
        fecha: '2023-11-31 15:35:00', descripcion: 'sin mivimiento', tipo: 'parada',
        colectivoRecorrido: { Colectivo: { unidad: '10' }, Recorrido: { denominacion: 'IDA', Linea: { denominacion: 'LINEA 2' } } },
      },
      {
        fecha: '2023-11-30 10:11:00', descripcion: 'sin mivimiento', tipo: 'ROTURA',
        colectivoRecorrido: { Colectivo: { unidad: '15' }, Recorrido: { denominacion: 'VUELTA', Linea: { denominacion: 'LINEA 1' } } },
      }
    ];
    this.notificacionesDS.data = data;

  }
}
