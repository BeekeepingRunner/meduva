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
  name: string = '';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.resetToken = params['resetToken'];

        this.userService.getUserWithResetToken(this.resetToken).subscribe(
          data => {
            this.name = data.name;
          }
        );
      }
    )
  }

}
