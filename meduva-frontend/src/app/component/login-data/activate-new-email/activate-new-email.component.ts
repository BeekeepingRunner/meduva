import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ResetTokenService} from "../../../service/token/reset-token.service";

@Component({
  selector: 'app-activate-new-email',
  templateUrl: './activate-new-email.component.html',
  styleUrls: ['./activate-new-email.component.css']
})
export class ActivateNewEmailComponent implements OnInit {

  emailToken: string = '';
  isTokenValid: boolean = false;
  responseReceived: boolean = false;

  errorMsg: string = '';

  constructor(
    private route: ActivatedRoute,
    private resetTokenService: ResetTokenService
  ) { }

  ngOnInit(): void {
    this.validateResetToken();
  }

  validateResetToken(){
    this.emailToken = this.route.snapshot.params.token;
    this.resetTokenService.validateEmailResetToken(this.emailToken).subscribe(
      data => {
        this.isTokenValid = true;
        this.responseReceived = true;
      },
      err => {
        this.isTokenValid = false;
        this.responseReceived = true;
        this.errorMsg = err.error.message;
      }
    );


  }
}
