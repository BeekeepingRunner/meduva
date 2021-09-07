import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {FormBuilder, FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register-copy.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {
    login: null,
    email: null,
    password: null,
    name: null,
    surname: null,
    phoneNumber: null
  };

  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      login: new FormControl('', [
                              Validators.required
      ]),
      email: new FormControl('', [
                              Validators.required
      ]),
      password: new FormControl('', [
                                Validators.required
      ]),
      name: new FormControl('', [
                            Validators.required
      ]),
      surname: new FormControl('', [
                                Validators.required
      ]),
      phoneNumber: new FormControl('', [
                                    Validators.required
      ]),


      }

    )
  }

  onSubmit(): void {

    const { login, email, password, name, surname, phoneNumber } = this.form;

    this.authService.register(login, email, password, name, surname, phoneNumber).subscribe(
      data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    )
  }
}
