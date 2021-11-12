import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {Client} from "../../../model/client";
import {User} from "../../../model/user";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {UserService} from "../../../service/user.service";
import {Term, VisitService} from "../../../service/visit.service";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";

@Component({
  selector: 'app-make-appointment',
  templateUrl: './make-appointment.component.html',
  styleUrls: ['./make-appointment.component.css']
})
export class MakeAppointmentComponent implements OnInit {

  currentUser!: User;

  worker!: User | null;
  client!: Client;
  service!: Service | null;

  isTermSelectionVisible = false;
  term!: Term | null;

  constructor(
    private jwtStorage: JwtStorageService,
    private userService: UserService,
    private visitService: VisitService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit(): void {
    // @ts-ignore
    this.userService.getUserDetails(this.jwtStorage.getCurrentUser()?.id).subscribe(
      currUser => {
        this.currentUser = currUser;
        this.client = {
          id : currUser.id,
          name: currUser.name,
          surname: currUser.surname,
          phoneNumber: currUser.phoneNumber,
          email: currUser.email
        }
      }
    );
  }

  onServiceSelection($event: any) {
    this.service = $event;
    this.worker = null;
    this.term = null;
  }

  onWorkerSelection($event: any) {
    this.worker = $event;
    this.term = null;
  }

  onReRenderSignal($event: boolean) {
    this.isTermSelectionVisible = false;
  }

  makeTermSelectionVisible() {
    this.isTermSelectionVisible = true;
  }

  onTermSelection($event: Term) {
    this.term = $event;
  }

  onVisitSubmit(): void {
    this.visitService.saveVisit(this.term).subscribe(
      visitData => {
        this.openFeedbackDialog();
      }, err => {
        console.log(err);
      }
    )
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: {message: 'Visit has been saved.'}
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/visit-history']);
      }
    );
  }
}
