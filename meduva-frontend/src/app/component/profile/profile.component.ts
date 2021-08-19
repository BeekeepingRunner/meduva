import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../service/token-storage.service";
import {User, UserService} from "../../service/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;

  dataUser!: User;

  constructor(
    private token: TokenStorageService,
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.currentUser = this.token.getCurrentUser();
    this.userService.getUserDetails(this.currentUser.login).subscribe(
      data => {
        this.dataUser = data;
      }
    );
    console.log(this.dataUser);
  }

}
