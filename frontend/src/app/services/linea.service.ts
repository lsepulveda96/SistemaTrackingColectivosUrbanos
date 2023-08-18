import { Injectable } from '@angular/core';

import { HTTPCONFIG,API  } from './httpconfig';
import { Linea } from '../data/linea';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recorrido } from '../data/recorrido';
import { Parada } from '../data/parada';

@Injectable({
  providedIn: 'root'
})
export class LineaService {

  constructor( private http: HttpClient) { }

  getLineas() : Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/lineas' );
  }

  getLinea( id: number ) : Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/linea/' + id );
  }

  saveLinea( linea: Linea ) : Observable<any> {
    return this.http.post( HTTPCONFIG.url + API + '/lineas', linea );
  }

  updateLinea( linea: Linea ) : Observable<any> {
    return this.http.put( HTTPCONFIG.url + API + '/linea/'+ linea.id, linea );
  }

  // -----------------  RECORRIDOS -----------------------------------------------------

  getRecorrido( idRecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/recorrido/' + idRecorrido );
  }
  
  getRecorridosActivos( idlinea: number  ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/recorridos/activo/' + idlinea );
  }

  getRecorridosNoActivos( idlinea: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/recorridos/noactivo/' + idlinea );
  }

  saveRecorrido( recorrido: Recorrido ): Observable<any>  {
    return this.http.post( HTTPCONFIG.url + API + '/recorridos' , recorrido );
  }

  updateRecorrido( idrec: number, recorrido: Recorrido ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + API + '/recorrido/' + idrec, recorrido  );
  }

  getParadasRecorrido( idrecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/recorrido/paradas/' + idrecorrido );
  }

  saveParadasRecorrido( idrecorrido: number, paradas: Parada[]): Observable<any> {
    return this.http.post( HTTPCONFIG.url + API + '/recorrido/paradas/' + idrecorrido, paradas );
  }  

  deactivateRecorrido( idRecorrido: number ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + API + '/recorrido/' + idRecorrido );
  }
}
