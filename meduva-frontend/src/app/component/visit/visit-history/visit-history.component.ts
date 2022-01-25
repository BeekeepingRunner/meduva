import { Component, OnInit } from '@angular/core';
import {VisitService} from "../../../service/visit.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {roleNames, UserRole} from "../../../model/user";
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
  displayedColumns: string[] = ['date', 'hour', 'serviceName', 'worker', 'room', 'status'];

  isWorker: boolean = false;
  asClientButtonColor: string = "primary";
  asWorkerButtonColor: string = "";

  now: Date = new Date();

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private jwtStorage: JwtStorageService,
    private roleGuardService: RoleGuardService
  ) { }

  ngOnInit(): void {
    if (this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_WORKER])) {
      this.isWorker = true;
    }

    this.currentUserId = this.jwtStorage.getCurrentUser()?.id;
    this.getAllVisitsAsClient();
  }

  onAsClientClick() {
    this.asClientButtonColor = "primary";
    this.asWorkerButtonColor = "";
    this.displayedColumns = ['date', 'hour', 'serviceName', 'worker', 'room', 'status'];
    this.getAllVisitsAsClient();
  }

  onAsWorkerClick() {
    this.asClientButtonColor = "";
    this.asWorkerButtonColor = "primary";
    this.displayedColumns = ['date', 'hour', 'serviceName', 'client', 'room', 'status'];
    this.getAllVisitsAsWorker();
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
    if (this.currentUserId) {
      this.visitService.getAllAsWorkerById(this.currentUserId).subscribe(
        visits => {
          console.log(visits);
          this.visits = visits
        }, err => {
          console.log(err);
        }
      );
    }
  }
}
