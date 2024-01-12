import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HTTPCONFIG, API } from './httpconfig';

@Injectable({
  providedIn: 'root'
})
export class MonitorService {

  data = [
    { id: 23, colectivo: { id: 1, unidad: 'colectivo 3', }, lineaDenominacion: 'linea 2', recorridoDenominacion: 'IDA', transito: true },
    { id: 25, colectivo: { id: 1, unidad: 'colectivo 4', }, lineaDenominacion: 'linea 2', recorridoDenominacion: 'REGRESO', transito: true },
    { id: 26, colectivo: { id: 1, unidad: 'colectivo 1', }, lineaDenominacion: 'linea 1', recorridoDenominacion: 'IDA', transito: true },
    { id: 55, colectivo: { id: 1, unidad: 'colectivo 5', }, lineaDenominacion: 'linea 5', recorridoDenominacion: 'IDA', transito: true },
    { id: 77, colectivo: { id: 1, unidad: 'colectivo 6', }, lineaDenominacion: 'linea 3', recorridoDenominacion: 'REGRESO', transito: true },
    { id: 83, colectivo: { id: 1, unidad: 'colectivo 22', }, lineaDenominacion: 'linea 1', recorridoDenominacion: 'REGRESO', transito: true }
  ];

  constructor(private http: HttpClient) { }

  getUnidadesTransito(): Observable<any> {
    return of({ error: false, codigo: 200, mensaje: 'lista de unidades en recorrido', data: this.data });
    //return  this.http.get( HTTPCONFIG.url + API + '/transito/unidades' );
  }

  getUnidadRecorridoTransito(idTransito: number): Observable<any> {
    const transit = this.data.find((it: any) => it.id == idTransito);
    return of({ error: false, codigo: 200, mensaje: 'transito ', data: transit })
    //return this.http.get(HTTPCONFIG.url + API + '/transito/unidad/' + idTransito);
  }

  getCoordenadasColectivoRecorrido(idColRec: number): Observable<any> {
    return this.http.get(HTTPCONFIG.url + API + '/transito/coordenadas/unidad/' + idColRec);
  }

  detenerColRec(idColRec: number, idLinea: number, disabled: boolean): Observable<any> {
    return this.http.delete(HTTPCONFIG.url + API + '/transito/detener/unidad/' + idColRec + '/' + idLinea + '/' + disabled);
  }

  getNotificacionesActivas(): Observable<any> {
    return this.http.get(HTTPCONFIG.url + API + '/notificacion/activas');
  }
}
