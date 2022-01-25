import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {VisitService} from "../../../service/visit.service";
import {Router} from "@angular/router";
import {ClientService} from "../../../service/client.service";
import {MatDialog} from "@angular/material/dialog";
import {ItemDayDialogComponent} from "../../../schedule/component/dialog/item-day-dialog/item-day-dialog.component";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";
import {RoleGuardService} from "../../../service/auth/role-guard.service";
import {JwtStorageService, TokenUserInfo} from "../../../service/token/jwt-storage.service";
import {roleNames, UserRole} from "../../../model/user";
import {UserService} from "../../../service/user.service";

@Component({
  selector: 'app-choose-service',
  templateUrl: './choose-service.component.html',
  styleUrls: ['./choose-service.component.css']
})
export class ChooseServiceComponent implements OnInit {

  currentUser!: TokenUserInfo | null;

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];

  errorMessage: string = '';

  constructor(
    private servicesService: ServicesService,
    private visitService: VisitService,
    private userService: UserService,
    private roleGuard: RoleGuardService,
    private jwtStorage: JwtStorageService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.currentUser = this.jwtStorage.getCurrentUser();

    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    )
  }

  submitService(service: Service): void {
    this.visitService.saveSelectedService(service);

    if (this.isUserMakingAnAppointment()) {
      this.askToPickWorker();
    } else {
      // @ts-ignore
      this.userService.getUserDetails(this.jwtStorage.getCurrentUser()?.id).subscribe(
        currentWorker => {
          this.visitService.saveSelectedWorker(currentWorker);
          this.router.navigate(['/visit/pick-term']);
        }
      );
    }
  }

  private askToPickWorker() {
    const confirmationDialog = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to select a worker?' }
    });

    confirmationDialog.afterClosed().subscribe(clientWantsToSelectWorker => {
      if (clientWantsToSelectWorker) {
        this.router.navigate(['/visit/pick-worker']);
      } else {
        // TODO: someday - generate terms without particular worker
        this.router.navigate(['/visit/pick-worker']);
      }
    });
  }

  private isUserMakingAnAppointment(): boolean {
    let visitClient = this.visitService.getSelectedClient();
    console.log(visitClient);
    return visitClient?.id == this.currentUser?.id
      && visitClient?.email != null;
  }
}
