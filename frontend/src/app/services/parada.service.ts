import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Parada } from '../data/parada';

import { HTTPCONFIG,API  } from './httpconfig';

@Injectable({
  providedIn: 'root'
})
export class ParadaService {

  constructor( private http: HttpClient ) { }

  getParadas() : Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/paradas' );
  }

  getParadasActivas(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/paradas/activas/' );
  }

  getParada( codigo: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/parada/' + codigo );
  }

  saveParada( parada: Parada ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + API + '/paradas', parada );
  }

  updateParada( parada: Parada ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + API + '/parada/' + parada.codigo, parada );
  }

  enableDisableParada( codigo: number, disabled: boolean ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + API + '/parada/activardesactivar/' + disabled + '/' + codigo );
  }
}
