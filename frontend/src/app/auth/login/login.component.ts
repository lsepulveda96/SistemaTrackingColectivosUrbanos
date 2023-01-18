import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TouchSequence } from 'selenium-webdriver';
import { AuthService } from 'src/app/services/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usernameIC = new UntypedFormControl('', Validators.required);
  passwIC = new UntypedFormControl('', [Validators.required, Validators.minLength(6)]);
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(private authService: AuthService,
    private tokenService: TokenStorageService, 
    private router: Router ) { }

  ngOnInit(): void {
    if (this.tokenService.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenService.getUser().roles;
    }
  }

  onSubmit() {
    this.authService.login(this.usernameIC.value, this.passwIC.value)
      .subscribe(data => {
        console.log("Result", data);
        this.tokenService.saveToken(data.token);
        this.tokenService.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenService.getUser().roles;
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
