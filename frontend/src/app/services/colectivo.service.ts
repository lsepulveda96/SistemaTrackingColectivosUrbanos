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

  uploadImagen( id: number, img: any ): Observable<any> {
    const formdata = new FormData();
    formdata.append('file', img );
    return this.http.post( HTTPCONFIG.url + API + '/files/image/upload/' +id, formdata );
  }

  downloadImagen( filename: string ):Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/files/img/download/'+filename, { responseType:'blob'} );
  }

  deleteImagen( filename: string ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + API + '/files/img/delete/'+filename );
  }

  uploadDoc( idColectivo:number, nombre:string, vence:boolean, vencimiento: Date, doc: any ): Observable<any> {
    const formdata = new FormData();
    formdata.append('nombre',nombre);
    formdata.append('vence',String(vence));
    formdata.append('vencimiento',vencimiento ? vencimiento.toISOString(): null );
    formdata.append('file', doc );
    return this.http.post( HTTPCONFIG.url + API + '/files/doc/upload/'+idColectivo, formdata );
  }

  updateDocFile( idDoc:number, nombre:string, vence:boolean, vencimiento: Date, doc: any ): Observable<any> {
    const formdata = new FormData();
    formdata.append('nombre',nombre);
    formdata.append('vence',String(vence));
    formdata.append('vencimiento',vencimiento ? vencimiento.toISOString(): null );
    formdata.append('file', doc );
    return this.http.post( HTTPCONFIG.url + API + '/files/doc/updatefile/'+idDoc, formdata );
  }

  updateDocData( id: number, doc: any ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + API + '/files/doc/updatedata/'+id, doc );
  }

  downloadDoc( filename: string  ):Observable<any> {
    return this.http.get( HTTPCONFIG.url + API + '/files/doc/download/'+filename, { responseType:'blob'} );
  }

  deleteDoc( docId:number ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + API + '/files/doc/delete/'+docId );
  }
}
