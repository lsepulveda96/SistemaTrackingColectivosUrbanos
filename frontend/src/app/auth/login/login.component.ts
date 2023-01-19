import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usernameIC = new UntypedFormControl('', Validators.required); // nombre de usuario
  passwIC = new UntypedFormControl('', [Validators.required, Validators.minLength(6)]); // password de usuario
  isLoggedIn = false;   // si existe usuario logueado
  isLoginFailed = false;// si fallo en intento de login
  errorMessage = '';    // mensaje de error si isLogginFailed=false

  constructor(private authService: AuthService,
    private tokenService: TokenStorageService, 
    private router: Router ) { }

  ngOnInit(): void {
    if (this.tokenService.getToken()) {
      this.isLoggedIn = true;
      this.goToPrincipal();
    }
    else {
      this.isLoggedIn = false;
    }
  }

  onSubmit() {
    this.authService.login(this.usernameIC.value, this.passwIC.value)
      .subscribe(data => {
        this.tokenService.saveToken(data.token);
        this.tokenService.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.goToPrincipal();
      }, err => {
        this.errorMessage = 'Usuario y/o contraseña incorrecta';
        this.isLoginFailed = true;
      });
  }

  goToPrincipal() {
    //window.location.reload();
    this.router.navigate(['dashboard']);
  }


}
