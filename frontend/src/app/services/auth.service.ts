import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

import { HTTPCONFIG  } from './httpconfig';

const AUTH_API = "/api/auth";

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

  register( username: string, email: string, pass: string ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + AUTH_API + '/signup', { username: username, password: pass, email: email }, httpOptions );
  }
}
