import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  resetToken: string = '';
  error: string = '';
  name: string = '';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
  ) { }

  // Identify a user based on a reset token from link which brought him here
  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.resetToken = params['resetToken'];

        this.userService.getUserWithResetToken(this.resetToken).subscribe(
          data => {
            this.name = data.name;
          },
          err => {
            this.error = err.error.message;
          }
        );
      }
    )
  }

  // Send password change request
  onSubmit() {

  }
}
