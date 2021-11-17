import { Injectable } from '@angular/core';

import { HTTPCONFIG  } from './httpconfig';
import { Linea } from '../data/linea';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HtmlParser } from '@angular/compiler';
import { RecorridoViewComponent } from '../views/lineas/recorrido-view/recorrido-view.component';
import { Recorrido } from '../data/recorrido';

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

  // -----------------  RECORRIDOS -----------------------------------------------------

  getRecorridoActivo( idlinea: number  ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorrido/activo/' + idlinea );
  }

  getRecorridosLinea( idlinea: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorridos/' + idlinea );
  }

  saveRecorrido( recorrido: Recorrido ): Observable<any>  {
    return this.http.post( HTTPCONFIG.url + '/recorrido', recorrido );
  }

  updateRecorrido( recorrido: Recorrido ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/recorrido/' + recorrido.id, recorrido  );
  }
}
