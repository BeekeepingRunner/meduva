import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../service/auth/auth.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form!: FormGroup;

  hide: boolean = true;

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
                              Validators.required,
                              Validators.minLength(5),
                              Validators.maxLength(20),
                              Validators.pattern('^[^-\\s]+$')

      ]),
      email: new FormControl('', [
                              Validators.required,
                              Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')
      ]),
      password: new FormControl('', [
                                Validators.required,
                                Validators.minLength(8),
                                Validators.maxLength(20),
                                Validators.pattern('^[^-\\t\\r\\n\\v\\f]+$'),
                                Validators.pattern('^[^-\\s]+$')
      ]),
      name: new FormControl('', [
                            Validators.required,
                            Validators.minLength(1),
                            Validators.maxLength(30),
                            Validators.pattern('^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.\'-]+$'),
                            Validators.pattern('^[^-\\s]+$')
      ]),
      surname: new FormControl('', [
                                Validators.required,
                                Validators.minLength(1),
                                Validators.maxLength(30),
                                Validators.pattern('^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.\'-]+$'),
                                Validators.pattern('^[^-\\s]+$')

      ]),
      phoneNumber: new FormControl('', [
                                    Validators.required,
                                    Validators.pattern('^(\\+[0-9]{1,4})?[0-9]{6,12}$')
      ]),
      }
    );
  }


  tryToSignUp(): void {

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

  onSubmit(){
    if(this.form.invalid){

      this.errorMessage = "Entered data must be correct";
      this.isSignUpFailed = true;
    }else{
      this.tryToSignUp();
    }
  }
}
