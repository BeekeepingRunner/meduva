import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../service/user.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {samePasswordsValidator} from "../../util/validator/same-passwords";
import {ResetTokenService} from "../../service/token/reset-token.service";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  resetToken: string = '';
  isTokenValid: boolean = false;

  error: string = '';

  isPasswordChanged: boolean = false;

  form!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private resetTokenService: ResetTokenService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.validateResetToken();
    this.buildForm();
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

  validateResetToken() {
    this.route.params.subscribe(
      params => {
        this.resetToken = params['resetToken'];
        this.resetTokenService.validateResetToken(this.resetToken).subscribe(
          data => {
            this.isTokenValid = true;
          },
          err => {
            this.isTokenValid = false;
            this.error = err;
          }
        );
      }
    );
  }

  resetPassword() {
    const passwordResetRequestBody = {
      resetToken: this.resetToken,
      password: this.form.controls.newPass.value,
      repeatPassword: this.form.controls.repeatNewPass.value
    };

    this.userService.resetPassword(passwordResetRequestBody).subscribe(
      data => {
        this.isPasswordChanged = true;
      },
      err => {
        this.error = err.error.message;
      }
    );
  }
}
