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
    private router: Router) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authReq = request;
    const token = this.token.getToken();
    if (token != null)
      authReq = request.clone({
        headers: request.headers.set(TOKEN_HEADER_KEY, "Bearer " + token),
      });
    console.log("Auth interceptor, authReq: ", authReq);
    return next.handle(authReq).pipe(
      tap({
        next: (event) => {
          console.log("Response Next event: ", event );
          if (event instanceof HttpResponse) {
            if (event.status == 401) {
              alert("Next Unauthorize access!!!");
            }
          }
          return event;
        },
        error: (error) => {
          console.log("Response Error event: ", error );
          if (error.status == 401) {
            alert("Error Unauthorize access!!!");
            this.token.signOut();
            this.router.navigate(['auth/login']);
          }
          if (error.status == 404)
            alert("Page Not Found!!!");
        }
      })
    );
  }
}

export const AuthInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
];
