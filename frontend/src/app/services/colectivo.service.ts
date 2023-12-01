import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Colectivo } from '../data/colectivo';

import { HTTPCONFIG, API  } from './httpconfig';

@Injectable({
  providedIn: 'root'
})
export class ColectivoService {

  constructor( private http: HttpClient ) { }

  getColectivos(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/colectivos');
  }

  getColectivo( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/colectivo/' + id );
  }

  saveColectivo( colectivo: Colectivo ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + API + '/colectivos', colectivo );
  }

  updateColectivo( colectivo: Colectivo ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + API + '/colectivo/' + colectivo.id, colectivo );
  }

  disableColectivo( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/colectivo/baja/' + id );
  }

  uploadImagen( img: any ): Observable<any> {
    const formdata = new FormData();
    formdata.append('file', img );
    return this.http.post( HTTPCONFIG.url + API + '/files/upload', formdata );
  }

  downloadImagen( filename: string ):Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/files/download/'+filename, { responseType:'blob'} );
  }

  deleteImagen( filename: string ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + API + '/files/delete/'+filename );
  }
}
