import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { HTTPCONFIG  } from './httpconfig';


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor( private http: HttpClient ) { }
  
  updateUsuario( id:number, isSuper: boolean,
    apellido: string, nombre: string, email: string, 
    dni: string, direccion: string, telefono: string ): Observable<any> {
    const payload = {  email: email, apellido: apellido, nombre: nombre, 
      dni: dni, direccion: direccion, telefono: telefono, superusuario: isSuper };
    return this.http.put( HTTPCONFIG.url + '/usuario/' + id, payload);
  }

  getUsuarios(): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/usuarios');
  }

  getUsuario( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/usuario/' + id );
  }

  deactivateUsuario( id: number ): Observable<any> {
    return this.http.delete( HTTPCONFIG.url + '/usuario/' + id );
  }

  activateUsuario( id: number ): Observable<any> {
    return this.http.get( HTTPCONFIG.url + '/usuario/activate/' + id );
  }
}
