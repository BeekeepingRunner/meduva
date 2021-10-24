import { Component, OnInit } from '@angular/core';
import {User} from "../../../../model/user";
import {UserService} from "../../../../service/user.service";

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
  ) { }

  ngOnInit(): void {
    this.userService.getAllWorkers().subscribe(
      this.setUsersMasterRoles
    );
  }

  setUsersMasterRoles = {
    next: (workers: any) => {
      this.workers = this.setMasterRoles(workers);
    }
  }

  private setMasterRoles(users: User[]) : User[] {
    users.forEach(user => {
      user.masterRole = this.userService.getMasterRole(user.roles);
    });
    return users;
  }
}
