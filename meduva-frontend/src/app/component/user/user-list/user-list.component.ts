import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../service/user.service";
import {User} from "../../../model/user";
import {JwtTokenStorageService} from "../../../service/token/jwt-token-storage.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  contentName = "Users";
  users: User[] = [];
  displayedColumns: string[] = ['id','name', 'surname', 'phoneNumber', 'email', 'role'];

  constructor(
    private jwtTokenStorageService: JwtTokenStorageService,
    private userService: UserService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.getAllUndeletedUsers();
  }

  public getAllUndeletedUsers(): void {

    this.userService.getAllUndeletedUsers().subscribe(
      this.setUsersMasterRoles
    );
    this.contentName = "Users";
  }

  public getAllWorkers(): void {
    this.userService.getAllWorkers().subscribe(
      this.setUsersMasterRoles
    );
    this.contentName = "Workers";
  }

  getAllClients() {
    this.userService.getAllClientsWithAccount().subscribe(
      this.setUsersMasterRoles
    );
    this.contentName = "Clients";
  }

  setUsersMasterRoles = {
    next: (users: any) => {
      this.users = this.setMasterRoles(users);
    }
  }

  private setMasterRoles(users: User[]) : User[] {
    users.forEach(user => {
      user.masterRole = this.userService.getMasterRole(user.roles);
    });
    return users;
  }

  getClientDetails(id: number): void{
    this.router.navigate(['/specific-user/'+id]);
  }
}
