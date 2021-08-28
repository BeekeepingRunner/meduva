import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../service/user.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {samePasswordsValidator} from "../../util/validator/same-passwords";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  resetToken: string = '';
  userEmail: string = '';
  error: string = '';

  form!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.buildForm();
    this.identifyUserFromToken();
  }

  private buildForm() {
    this.form = this.formBuilder.group(
      {
        newPass : new FormControl('', [
          Validators.minLength(8),
          Validators.maxLength(20),
          Validators.required
        ]),
        repeatNewPass : new FormControl('', [
          Validators.required
        ])
      },
      { validators : samePasswordsValidator });
  }

  identifyUserFromToken() {
    this.route.params.subscribe(
      params => {
        this.resetToken = params['resetToken'];
        /*
        this.userService.getEmailFromResetToken(this.resetToken).subscribe(
          data => {
            this.userEmail = data;
          },
          err => {
            this.error = err.error.message;
          }
        );
        */
      }
    );
  }

  // Send password change request to the backend
  onSubmit() {
    const passwordResetRequestBody = {
      resetToken: this.resetToken,
      password: this.form.controls.newPass.value,
      repeatPassword: this.form.controls.repeatNewPass.value
    };

    this.userService.resetPassword(passwordResetRequestBody).subscribe(
      data => {
        console.log(data);
      },
      err => {
        console.log(err);
      }
    );
  }
}
