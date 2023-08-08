import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AccessRoutesGuard implements CanActivate {


  constructor( 
    private authTokenService: TokenStorageService,
    private router: Router ) {}
    
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const admin = this.authTokenService.isUserAdmin();
      console.log("Usuario admin: ", admin);
      if (!admin)
        this.router.navigate(['misc/no-authorized']);
      return admin;
  }
  
}
