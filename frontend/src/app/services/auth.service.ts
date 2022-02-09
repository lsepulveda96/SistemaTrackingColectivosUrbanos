import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HTTPCONFIG  } from './httpconfig';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient ) { }

  login( usernam: string, password: string ): Observable<any> {
    return this.http.post( HTTPCONFIG.url + '/sigin', { usernam, password });
  }
}
