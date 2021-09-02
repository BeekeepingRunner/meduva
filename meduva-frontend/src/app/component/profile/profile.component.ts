import { Component, OnInit } from '@angular/core';
import {JwtTokenStorageService} from "../../service/token/jwt-token-storage.service";
import {Role, UserService} from "../../service/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;
  userDetails: any;
  userRole!: Role;
  error!: string;

  constructor(
    private token: JwtTokenStorageService,
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.currentUser = this.token.getCurrentUser();
    if (this.currentUser) {
      this.userService.getUserDetails(this.currentUser.id).subscribe(
        data => {
          this.userDetails = data;
          this.userRole = this.userService.getMasterRole(this.userDetails.roles);
        },
        err => {
          this.error = err.getError();
        }
      );
    }
  }

}
