import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Parada } from '../data/parada';

import { HTTPCONFIG  } from './httpconfig';

@Injectable({
  providedIn: 'root'
})
export class ParadaService {

  constructor( private http: HttpClient ) { }

  getParadas() : Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/paradas' );
  }

  getParada( codigo: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/parada/' + codigo );
  }

  saveParada( parada: Parada ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/paradas', parada );
  }

  updateParada( parada: Parada ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/parada/' + parada.codigo, parada );
  }
}
