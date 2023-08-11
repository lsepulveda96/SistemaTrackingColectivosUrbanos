import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AccessRoutesGuard implements CanActivate {


  constructor( 
    private authTokenService: TokenStorageService,
    private router: Router,
    private location: Location ) {}
    
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const admin = this.authTokenService.isUserAdmin();
      if (!admin)
        // this.location.back();
        this.router.navigate(['misc/no-authorized']);
      return admin;
  }
  
}
