import { Component, OnInit } from '@angular/core';
import {User, UserService} from "../../service/user.service";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  contentName = "Users";
  users: User[] = [];
  displayedColumns: string[] = ['name', 'surname', 'phoneNumber', 'email', 'role'];

  constructor(
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.getAllUsers();
  }

  public getAllUsers(): void {
    
    this.userService.getAllUsers().subscribe(
      users => {
        this.users = this.setMasterRoles(users);
      }
    );
    this.contentName = "Users";
  }

  private setMasterRoles(users: User[]) : User[] {
    users.forEach(user => {
      user.masterRole = this.userService.getMasterRole(user.roles);
    });
    return users;
  }

  public getAllWorkers(): void {
    this.userService.getAllWorkers().subscribe(
      workers => {
        this.users = this.setMasterRoles(workers);
      }
    );
    this.contentName = "Workers";
  }
}
