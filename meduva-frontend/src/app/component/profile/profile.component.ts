import { Component, OnInit } from '@angular/core';
import {JwtTokenStorageService, TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {UserService} from "../../service/user.service";
import {Role, User} from "../../model/user";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser!: TokenUserInfo | null;
  userDetails!: User;
  userRole!: Role;
  error!: string;

  constructor(
    private token: JwtTokenStorageService,
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.currentUser = this.token.getCurrentUser();
    if (this.currentUser != null) {
      this.getUserDetails(this.currentUser.id);
    }
  }

  private getUserDetails(userId: number): void {

    this.userService.getUserDetails(userId).subscribe(
      (data: User) => {
        this.userDetails = data;
        this.userRole = this.userService.getMasterRole(this.userDetails.roles);
      },
      err => {
        this.error = err.getError();
      }
    );
  }
}
