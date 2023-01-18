import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { HTTPCONFIG  } from './httpconfig';
import { Usuario } from '../data/usuario';

const API_TEST = '/api/test';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor( private http: HttpClient ) { }

  getUsuarios(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/usuarios');
  }

  getUsuario( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/usuario/' + id );
  }

  saveUsuario( usuario: Usuario ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/usuarios', usuario );
  }

  updateUsuario( usuario: Usuario ): Observable<any> {
    return this.http.put( HTTPCONFIG.url + '/usuario/' + usuario.id, usuario  );
  }

  // --------------- TEST

  getPublicContent(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API_TEST +'/all');
  }

  getUserBoard(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + API_TEST + '/user');
  }

  getAdminBoard() : Observable<any> {
    return this.http.get( HTTPCONFIG.url + API_TEST + '/admin');
  }
}
