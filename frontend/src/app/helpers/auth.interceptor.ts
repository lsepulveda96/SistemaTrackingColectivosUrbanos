import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
  HttpResponse,
} from "@angular/common/http";
import { Observable } from "rxjs";
import { TokenStorageService } from "../services/token-storage.service";
import { tap } from "rxjs/operators";
import { Router } from "@angular/router";

// Para Spring boot backend
const TOKEN_HEADER_KEY = "Authorization";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private token: TokenStorageService,
    private router: Router) { }

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authReq = request;
    const token = this.token.getToken();
    if (token != null) {
      authReq = request.clone({
        headers: request.headers.set(TOKEN_HEADER_KEY, "Bearer " + token),
      });
      return next.handle(authReq).pipe(
        tap({
          next: (event) => {
            if (event instanceof HttpResponse) {
              if (event.status == 401) {
                this.token.signOut();
                this.router.navigate(['auth/login']);
              }
            }
            return event;
          },
          error: (error) => {
            if (error.status == 401) {
              this.token.signOut();
              this.router.navigate(['auth/login']);
            }
            if (error.status == 404)
              alert("Page Not Found!!!");
          }
        })
      );
    }
    else {
      return next.handle(authReq);
    }
  }
}

export const AuthInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
];
