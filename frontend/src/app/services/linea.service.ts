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

  getRecorrido( idRecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/linea/recorrido/' + idRecorrido );
  }
  getRecorridoActivo( idlinea: number  ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/linea/recorrido/activo/' + idlinea );
  }

  getRecorridosLinea( idlinea: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/linea/recorridos/' + idlinea );
  }

  saveRecorrido( recorrido: Recorrido ): Observable<any>  {
    return this.http.post( HTTPCONFIG.url + '/linea/recorrido' , recorrido );
  }

  updateRecorrido( recorrido: Recorrido ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/linea/recorrido/' + recorrido.id, recorrido  );
  }

  getParadasRecorrido( idrecorrido: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/linea/paradas/' + idrecorrido );
  }

  
}
