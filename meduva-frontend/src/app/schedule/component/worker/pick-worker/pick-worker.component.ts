import { Component, OnInit } from '@angular/core';
import {User} from "../../../../model/user";
import {UserService} from "../../../../service/user.service";
import {JwtTokenStorageService} from "../../../../service/token/jwt-token-storage.service";

@Component({
  selector: 'app-pick-worker',
  templateUrl: './pick-worker.component.html',
  styleUrls: ['./pick-worker.component.css']
})
export class PickWorkerComponent implements OnInit {

  workers: User[] = [];
  displayedColumns: string[] = ['name', 'surname', 'phoneNumber', 'email', 'role'];

  constructor(
    private userService: UserService,
    private jwtTokenStorageService: JwtTokenStorageService,
  ) { }

  ngOnInit(): void {
    this.userService.getAllWorkers().subscribe(
      workers => {
        workers = this.deleteCurrentUser(workers);
        this.workers = this.setMasterRoles(workers);
      }, err => {
        console.log(err);
      }
    );
  }

  private setMasterRoles(users: User[]) : User[] {
    users.forEach(user => {
      user.masterRole = this.userService.getMasterRole(user.roles);
    });
    return users;
  }

  private deleteCurrentUser(registeredClients: User[]): User[] {
    let currentUserId = this.jwtTokenStorageService.getCurrentUser()?.id;
    return registeredClients.filter(client => client.id != currentUserId);
  }
}
