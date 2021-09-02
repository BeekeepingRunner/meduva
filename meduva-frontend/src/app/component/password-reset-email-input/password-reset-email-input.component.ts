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
  resultInfo: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private emailService: EmailService,
  ) {}

  // Builds form with validated email input field
  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$')
      ]),
    });
  }

  // Sends mail with reset link or displays error
  onSubmit() {
    this.submitted = true;
    this.resultInfo = 'Please, wait for a moment...';

    this.emailService.sendResetLinkMail(this.form.get('email')?.value).subscribe(
      this.sentMailMessageObserver
    )
  }

  sentMailMessageObserver = {
    next: (data: any) => {
      this.emailSent = true;
      this.resultInfo = 'Check your mailbox for password-reset link!';
    },
    error: (err: any) => {
      this.resultInfo = err.error.message;
    }
  }
}
