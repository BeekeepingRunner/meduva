import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {JwtTokenStorageService, TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {
    login: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private tokenStorage: JwtTokenStorageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.hasJustLoggedIn()) {
      this.router.navigate(['/profile']);
    }
  }

  hasJustLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

  tryToLogIn(): void {
    const { login, password } = this.form;
    this.authService.login(login, password).subscribe(

      (userInfo: TokenUserInfo) => {
        this.changeStateAfterLogin(userInfo);
        this.reloadPage();  // so that the toolbar and sidebar updates
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  private changeStateAfterLogin(userInfo: TokenUserInfo): void {
    this.tokenStorage.saveToken(userInfo.accessToken);
    this.tokenStorage.saveUser(userInfo);

    this.isLoginFailed = false;
    this.isLoggedIn = true;
  }

  reloadPage(): void {
    window.location.reload();
  }

}
