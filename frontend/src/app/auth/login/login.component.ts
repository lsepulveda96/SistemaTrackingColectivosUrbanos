import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usernameIC = new UntypedFormControl('', Validators.required );
  passwIC = new UntypedFormControl('', [Validators.required, Validators.minLength(6)]);
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor( private authService: AuthService,
              private tokenService: TokenStorageService ) { }

  ngOnInit(): void {
    if (this.tokenService.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenService.getUser().roles;
    }
  }

  onSubmit() {
    this.authService.login( this.usernameIC.value, this.passwIC.value )
      .subscribe( result => {
        if (!result.error) {
          this.tokenService.saveToken( result.data.accessToken );
          this.tokenService.saveUser( result.data );
          this.isLoggedIn = true;
          this.roles = this.tokenService.getUser().roles;
          this.goToPrincipal();
        }
        else {
          this.errorMessage = result.mensaje;
          this.isLoginFailed = true;
        }
      });
  }

  goToPrincipal() {
    window.location.reload();
  }


}
