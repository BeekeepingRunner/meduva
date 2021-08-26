import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {EmailService} from "../../service/email.service";

@Component({
  selector: 'app-password-reset-email-input',
  templateUrl: './password-reset-email-input.component.html',
  styleUrls: ['./password-reset-email-input.component.css']
})
export class PasswordResetEmailInputComponent implements OnInit {

  form!: FormGroup;
  submitted: boolean = false;
  emailSent: boolean = false;
  info: string = '';
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private emailService: EmailService,
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$')
      ]),
    });
  }

  onSubmit() {
    this.submitted = true;
    this.info = 'Please, wait for a moment...';

    this.emailService.sendResetLinkMail(this.form.get('email')?.value).subscribe(
      data => {
        this.emailSent = true;
        this.info = 'Check your mailbox for password-reset link!';
      },
      err => {
        this.info = err.error.message;
      }
    )
  }
}
