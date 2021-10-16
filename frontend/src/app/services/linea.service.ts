import { Injectable } from '@angular/core';

import { HTTPCONFIG  } from './httpconfig';
import { Linea } from '../data/linea';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LineaService {

  constructor( private http: HttpClient) { }

  getLineas() : Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/lineas' );
  }

  getLinea( id: number ) : Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/linea/' + id );
  }

  saveLinea( linea: Linea ) : Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/lineas', linea );
  }

  updateLinea( linea: Linea ) : Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/linea/'+ linea.id, linea );
  }
}
