import { Component, OnInit } from '@angular/core';
import {EmailService} from "../../../service/email.service";
import {JwtStorageService, TokenUserInfo} from "../../../service/token/jwt-storage.service";
import {UserService} from "../../../service/user.service";
import {DefaultUrlSerializer} from "@angular/router";
import {User} from "../../../model/user";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  emailSent: boolean = false;
  sendingFailed: boolean = false;
  resultInfo: string = '';
  tokenUserInfo!: TokenUserInfo | null;
  user?: User;

  constructor(
      private emailService: EmailService,
      private userService: UserService,
      private token: JwtStorageService
  ) { }

  ngOnInit(): void {
    this.tryToSendPasswordResetMail();
  }

  tryToSendPasswordResetMail(){
    this.tokenUserInfo = this.token.getCurrentUser();
    if(this.tokenUserInfo?.id != null){
      this.sendEmailResetLink();
    }
  }

  sendEmailResetLink(){
    let userId = this.tokenUserInfo?.id;
    this.userService.getUserDetails(userId!).subscribe(
      data => {
        this.user = data;
        this.emailService.sendResetLinkMail(this.user!.email).subscribe(
          this.sentMailMessageObserver
        )
      }, err => {
        this.sendingFailed = true;
        this.resultInfo = err.error.message;
      }
    )
  }

  sentMailMessageObserver = {
    next: (data: any) => {
      this.emailSent = true;
      this.resultInfo = 'Check your mailbox for password-reset link!';
    },
    error: (err: any) => {
      this.sendingFailed = true;
      this.resultInfo = err.error.message;
    }
  }
}
