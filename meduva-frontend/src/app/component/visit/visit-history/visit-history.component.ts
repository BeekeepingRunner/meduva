import { Component, OnInit } from '@angular/core';
import {VisitService} from "../../../service/visit.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {roleNames, User, UserRole} from "../../../model/user";
import {UserService} from "../../../service/user.service";
import {Visit} from "../../../model/visit";
import {RoleGuardService} from "../../../service/auth/role-guard.service";

@Component({
  selector: 'app-visit-history',
  templateUrl: './visit-history.component.html',
  styleUrls: ['./visit-history.component.css']
})
export class VisitHistoryComponent implements OnInit {

  currentUserId!: number | undefined;

  visits: Visit[] = [];

  displayedColumns: string[] = ['date', 'hour', 'serviceName', 'room', 'status'];

  isWorker: boolean = false;

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private jwtStorage: JwtStorageService,
    private roleGuardService: RoleGuardService
  ) { }

  ngOnInit(): void {
    if (this.roleGuardService.hasExpectedRole(roleNames[UserRole.ROLE_WORKER])) {
      this.isWorker = true;
    }

    this.currentUserId = this.jwtStorage.getCurrentUser()?.id;
    this.getAllVisitsAsClient();
  }

  getAllVisitsAsClient() {
    if (this.currentUserId) {
      this.visitService.getAllAsClientByUserId(this.currentUserId).subscribe(
        visits => {
          console.log(visits);
          this.visits = visits
        }, err => {
          console.log(err);
        }
      );
    }
  }

  getAllVisitsAsWorker() {

  }
}
