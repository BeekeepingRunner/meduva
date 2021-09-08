import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {JwtTokenStorageService, TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {Router} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form!: FormGroup;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  hide = true;

  constructor(
    private authService: AuthService,
    private tokenStorage: JwtTokenStorageService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      login: new FormControl('', [
                              Validators.required,
      ]),
      password: new FormControl('', [
                              Validators.required
      ])

    });


    if (this.hasJustLoggedIn()) {
      this.router.navigate(['/profile']);
    }
  }

  hasJustLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

  tryToLogIn(): void {
    const login: string = this.form.controls.login.value;
    const password: string = this.form.controls.password.value;

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
