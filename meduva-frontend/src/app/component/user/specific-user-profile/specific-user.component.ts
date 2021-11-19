import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../service/user.service";
import {Role, User} from "../../../model/user";
import {ActivatedRoute, Router} from "@angular/router";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {Service} from "../../../model/service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {ConfirmationWithWarningDialogComponent} from "../../dialog/confirmation-with-warning-dialog/confirmation-with-warning-dialog.component";
import {Visit} from "../../../model/visit";
import {Client} from "../../../model/client";
import {VisitService} from "../../../service/visit.service";

@Component({
  selector: 'app-specific-user',
  templateUrl: './specific-user.component.html',
  styleUrls: ['./specific-user.component.css']
})
export class SpecificUserComponent implements OnInit {

  userId!: number;
  userDetails!: User;
  userRole!: Role;
  isAWorker: boolean = false;
  workerServices!: Service[];
  columnName: string[] = ['Name'];
  error!: string;
  userVisits: Visit[] = [];
  clientDetails!: Client;

  constructor(
    private userService: UserService,
    private visitService: VisitService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog,
    private router: Router,
    private currentUserJwtToken: JwtStorageService

  ) { }

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params.id;
    if (this.userId != null) {
      this.getUserDetails(this.userId);
      this.getWorkerSerivces();
    }

  }

  onUserVisitsGot($event: Visit[]){
    this.userVisits = $event;
  }


  private getUserDetails(userId: number): void {

    this.userService.getUserDetails(userId).subscribe(
      (data: User) => {
        this.userDetails = data;
        this.userRole = this.userService.getMasterRole(this.userDetails.roles);
        this.isUserAWorker();
      },
      err => {
        this.error = err.getError();
      }
    );
  }

  getWorkerSerivces(){
    this.userService.getWorkerServices(this.userId).subscribe(
      services => {
        this.workerServices = services;
      }
    )
  }

  isUserAWorker(){
    this.isAWorker = this.userRole.name != 'ROLE_CLIENT';
  }

  openDeleteConfirmDialog(): void {
    if(this.userId==this.currentUserJwtToken.getCurrentUser()?.id){
      const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
        data: { message: 'U cannot delete yourself' }
      });
    }
    else{
      const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: { message: 'Are u sure you want to delete this user?' }
      });

      confirmDialogRef.afterClosed().subscribe(confirmed => {
        if (confirmed) {
          if(this.userVisits.length>0){
            this.openDeleteWarningDialog();
          }
          else{
            this.deleteUser();
          }
        }
      });
    }
  }

  deleteUser() {
    this.visitService.deleteAllAsClientByUserId(this.userId).subscribe();
    if(this.isAWorker==true){
      this.visitService.deleteAllAsWorkerByUserId(this.userId).subscribe();
    }
    this.userService.deleteById(this.userId).subscribe(
      ifSuccess => {
        this.openFeedbackDialog();
      }

    );
  }

  openDeleteWarningDialog(): void {
    const warningConfirmDialogRef = this.dialog.open(ConfirmationWithWarningDialogComponent, {
      data: { message: 'The customer has visits, if you delete him, you will also delete the visits to which he is assigned!' }
    });

    warningConfirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteUser();
      }
    });
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'User ' + this.userDetails.name + ' has been deleted.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/users']);
      }
    );
  }

}
