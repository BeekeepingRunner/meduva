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
  error: string = '';
  id?: number;

  form!: FormGroup;
  inputError: string = '';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {

    this.form = this.formBuilder.group(
      {
        newPass : new FormControl('', [
          Validators.minLength(8),
          Validators.maxLength(20)
        ]),
        repeatNewPass : new FormControl('')
      },
      { validators : samePasswordsValidator });

    this.identifyUserFromToken();
  }

  identifyUserFromToken() {
    this.route.params.subscribe(
      params => {
        this.resetToken = params['resetToken'];

        this.userService.getUserWithResetToken(this.resetToken).subscribe(
          data => {
            this.id = data.id;
          },
          err => {
            this.error = err.error.message;
          }
        );
      }
    );
  }

  // Send password change request
  onSubmit() {
    
  }
}
