import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorService } from 'src/app/services/monitor.service';

@Component({
  selector: 'app-enrecorrido-list',
  templateUrl: './enrecorrido-list.component.html',
  styleUrls: ['./enrecorrido-list.component.css']
})
export class EnrecorridoListComponent implements OnInit, AfterViewInit {

  waiting: boolean;
  transito: any[];
  transitoDS: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  @ViewChild('pag') pag: MatPaginator;

  constructor( private servicioMonitor: MonitorService) { }

  ngOnInit(): void {
    this.getUnidadesEnRecorrido();
  }

  ngAfterViewInit(): void {
    this.transitoDS.paginator = this.pag;    
  }

  getUnidadesEnRecorrido() {
    console.log("Unidades en recorrido: ");
    this.waiting = true;
    this.servicioMonitor.getUnidadesTransito().subscribe( result => {
      this.waiting = false;
      this.transitoDS.data = result.data;
    });

    this.transito = [
      { colectivo: {id: 1, unidad: 'colectivo 3', }, linea: {id: 2, denominacion:'linea 2', }, recorrido: {id: 1, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 4', }, linea: {id: 2, denominacion:'linea 2', }, recorrido: {id: 2, denominacion: 'REGRESO'} },
      { colectivo: {id: 1, unidad: 'colectivo 1', }, linea: {id: 2, denominacion:'linea 1', }, recorrido: {id: 2, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 5', }, linea: {id: 2, denominacion:'linea 5', }, recorrido: {id: 2, denominacion: 'IDA'} },
      { colectivo: {id: 1, unidad: 'colectivo 6', }, linea: {id: 2, denominacion:'linea 3', }, recorrido: {id: 2, denominacion: 'REGRESO'} },
      { colectivo: {id: 1, unidad: 'colectivo 22',}, linea: {id: 2, denominacion:'linea 1',}, recorrido: {id: 2,  denominacion: 'REGRESO'} }
    ]; 
    this.transitoDS.data = this.transito;
  }
}
