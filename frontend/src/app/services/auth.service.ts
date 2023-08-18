import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HTTPCONFIG,API  } from './httpconfig';

const httpOptions = {
  headers: new HttpHeaders( {'Content-type':'application/json'})
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient ) { }


  login( user: string, passwd: string ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + API + '/auth/signin', { username: user, password: passwd }, httpOptions );
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
    return this.http.post( HTTPCONFIG.url + API + '/auth/signup', 
      payload , httpOptions );
  }
}
