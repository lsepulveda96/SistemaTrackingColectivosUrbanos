import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: []
})
export class AppHeaderComponent implements OnInit {
  username?: string;
  isLoggedIn: boolean;
  private authListenerSubs: Subscription;

  constructor( 
    private tokenService: TokenStorageService,
    private router: Router ) {
  }

  ngOnInit() {
    this.isLoggedIn = !!this.tokenService.getToken();
    this.username = this.tokenService.getUser()?.username;

    this.authListenerSubs = this.tokenService.getAuthStatusListener().subscribe( isauth => {
      this.isLoggedIn = isauth;
      const usr = this.tokenService.getUser();
      this.username = usr ? usr.username: null;
    });
  }

  ngOnDestroy() {
    this.authListenerSubs.unsubscribe();
  }

  iniciarSesion() {
    this.router.navigate(['auth/login']);
  }

  cerrarSesion() {
    this.tokenService.signOut();
    this.router.navigate(['auth/login']);
  }

  goToPerfil() {
    console.log("got to perfil");
    this.router.navigate( ['perfil'])
  }

}
