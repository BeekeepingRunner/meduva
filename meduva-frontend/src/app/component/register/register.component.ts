import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form!: FormGroup;

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

    const login: string = this.form.controls.login.value;
    const email: string = this.form.controls.email.value;
    const password: string = this.form.controls.password.value;
    const name: string = this.form.controls.name.value;
    const surname: string = this.form.controls.surname.value;
    const phoneNumber: string = this.form.controls.phoneNumber.value;

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
