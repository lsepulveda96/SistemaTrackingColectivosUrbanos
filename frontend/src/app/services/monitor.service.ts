import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HTTPCONFIG } from './httpconfig';

@Injectable({
  providedIn: 'root'
})
export class MonitorService {

  constructor( private http: HttpClient ) { }

  getUnidadesTransito(): Observable<any> {
    return  this.http.get( HTTPCONFIG.url + '/transito/unidades' );
  }

  getUnidadRecorridoTransito( idTransito: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/transito/unidad/' + idTransito );
  }

}
