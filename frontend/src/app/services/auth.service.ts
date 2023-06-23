import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { stringToKeyValue } from '@angular/flex-layout/extended/typings/style/style-transforms';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

import { HTTPCONFIG  } from './httpconfig';

const AUTH_API = "/v1/auth";

const httpOptions = {
  headers: new HttpHeaders( {'Content-type':'application/json'})
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient ) { }


  login( user: string, passwd: string ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + AUTH_API + '/signin', { username: user, password: passwd }, httpOptions );
  }

  logout() {
  }

  register( username: string, pass: string, isSuper: boolean,
    apellido: string, nombre: string, email: string, 
    dni: string, direccion: string, telefono: string ): Observable<any> {
    const roles = isSuper ? ['admin','usr']: ['usr'];
    const payload = { username: username, password: pass, email: email, 
      apellido: apellido, nombre: nombre, dni: dni, direccion: direccion, telefono: telefono,
      roles: roles };
    return this.http.post( HTTPCONFIG.url + AUTH_API + '/signup', 
      payload , httpOptions );
  }
}
