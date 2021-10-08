import { Component, OnInit } from '@angular/core';
import {JwtTokenStorageService, TokenUserInfo} from "../../service/token/jwt-token-storage.service";
import {UserService} from "../../service/user.service";
import {Role, User} from "../../model/user";
import {ActivatedRoute, Router} from "@angular/router";
import {ConfirmationDialogComponent} from "../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {FeedbackDialogComponent} from "../dialog/feedback-dialog/feedback-dialog.component";

@Component({
  selector: 'app-specific-user',
  templateUrl: './specific-user.component.html',
  styleUrls: ['./specific-user.component.css']
})
export class SpecificUserComponent implements OnInit {

  userId!: number;
  userDetails!: User;
  userRole!: Role;
  error!: string;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog,
    private router: Router

  ) { }

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params.id;
    if (this.userId != null) {
      this.getUserDetails(this.userId);
    }
  }

  private getUserDetails(userId: number): void {

    this.userService.getUserDetails(userId).subscribe(
      (data: User) => {
        this.userDetails = data;
        this.userRole = this.userService.getMasterRole(this.userDetails.roles);
      },
      err => {
        this.error = err.getError();
      }
    );
  }

  openDeleteConfirmDialog(): void {
    const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to delete this user?' }
    });

    confirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteUser();
      }
    });
  }

  deleteUser() {
    this.userService.deleteById(this.userId).subscribe(
      ifSuccess => {
        this.openFeedbackDialog();
      }

    );
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
