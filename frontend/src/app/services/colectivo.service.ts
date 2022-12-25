import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Colectivo } from '../data/colectivo';

import { HTTPCONFIG  } from './httpconfig';
@Injectable({
  providedIn: 'root'
})
export class ColectivoService {

  constructor( private http: HttpClient ) { }

  getColectivos(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/colectivos');
  }

  getColectivo( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/colectivo/' + id );
  }

  saveColectivo( colectivo: Colectivo ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/colectivos', colectivo );
  }

  updateColectivo( colectivo: Colectivo ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/colectivo/' + colectivo.id, colectivo );
  }

  disableColectivo( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/colectivo/baja/' + id );
  }
}
