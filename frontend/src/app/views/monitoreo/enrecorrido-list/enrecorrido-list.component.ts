import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatTabLabel } from '@angular/material/tabs';

@Component({
  selector: 'app-enrecorrido-list',
  templateUrl: './enrecorrido-list.component.html',
  styleUrls: ['./enrecorrido-list.component.css']
})
export class EnrecorridoListComponent implements OnInit, AfterViewInit {

  transito: any[];
  transitoDS: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  @ViewChild('pag') pag: MatPaginator;

  constructor() { }

  ngOnInit(): void {
    this.getUnidadesEnRecorrido();
  }

  ngAfterViewInit(): void {
    this.transitoDS.paginator = this.pag;    
  }

  getUnidadesEnRecorrido() {
    console.log("Unidades en recorrido: ");
    this.transito = [
      { unidad: 'colectivo 3', linea:'linea 2', recorrido: 'IDA'},
      { unidad: 'colectivo 4', linea:'linea 2', recorrido: 'REGRESO'},
      { unidad: 'colectivo 1', linea:'linea 1', recorrido: 'IDA'},
      { unidad: 'colectivo 5', linea:'linea 5', recorrido: 'IDA'},
      { unidad: 'colectivo 6', linea:'linea 3', recorrido: 'REGRESO'},
      { unidad: 'colectivo 22', linea:'linea 1', recorrido: 'REGRESO'}
    ];
    this.transitoDS.data = this.transito;
  }
}
