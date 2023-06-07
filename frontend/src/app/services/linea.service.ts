import { Injectable } from '@angular/core';

import { HTTPCONFIG  } from './httpconfig';
import { Linea } from '../data/linea';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HtmlParser } from '@angular/compiler';
import { RecorridoViewComponent } from '../views/lineas/recorrido-view/recorrido-view.component';
import { Recorrido } from '../data/recorrido';
import { Parada } from '../data/parada';

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

  getRecorrido( idRecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorrido/' + idRecorrido );
  }
  
  getRecorridosActivos( idlinea: number  ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorridos/activo/' + idlinea );
  }

  getRecorridosNoActivos( idlinea: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorridos/noactivo/' + idlinea );
  }

  saveRecorrido( recorrido: Recorrido ): Observable<any>  {
    return this.http.post( HTTPCONFIG.url + '/recorridos' , recorrido );
  }

  updateRecorrido( idrec: number, recorrido: Recorrido ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/recorrido/' + idrec, recorrido  );
  }

  getParadasRecorrido( idrecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/recorrido/paradas/' + idrecorrido );
  }

  saveParadasRecorrido( idrecorrido: number, paradas: Parada[]): Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/recorrido/paradas/' + idrecorrido, paradas );
  }  
}
