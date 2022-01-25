import {Component, Injector, OnInit} from '@angular/core';
import {Visit} from "../../../model/visit";
import {VisitService} from "../../../service/visit.service";
import {RoleGuardService} from "../../../service/auth/role-guard.service";
import {ActivatedRoute} from "@angular/router";
import {roleNames, UserRole} from "../../../model/user";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DayDialogComponent, DayDialogData} from "../../../schedule/component/dialog/day-dialog/day-dialog.component";
import {EditVisitTermComponent} from "../edit-visit-term/edit-visit-term.component";

export interface VisitDetailsDialogData {
  visitId: number;
}

@Component({
  selector: 'app-visit-details',
  templateUrl: './visit-details.component.html',
  styleUrls: ['./visit-details.component.css']
})

export class VisitDetailsComponent implements OnInit {

  visit!: Visit;
  isClient: boolean = true;
  isWorker: boolean = true;
  isReceptionist: boolean = true;
  data!: VisitDetailsDialogData;
  dialogRef: any;
  dialogData: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private visitService: VisitService,
    private roleGuard: RoleGuardService,
    private injector: Injector,
    private dialog: MatDialog,
  ) {
    this.dialogRef = this.injector.get(MatDialogRef, null);
    this.dialogData = this.injector.get(MAT_DIALOG_DATA, null);
  }

  ngOnInit(): void {
    let visitId = this.activatedRoute.snapshot.params.id;
    this.isWorker = this.roleGuard.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_WORKER]);
    this.isReceptionist = this.roleGuard.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_RECEPTIONIST]);
    if (visitId) {
      this.getVisitDetails(visitId);
    } else {
      this.data = this.injector.get(MAT_DIALOG_DATA);
      this.getVisitDetails(this.data!.visitId);
    }
  }

  private getVisitDetails(visitId: number) {
    this.visitService.getVisitById(visitId).subscribe(
      visit => {
        this.visit = visit;
        console.log(this.visit);
      }, err => {
        console.log(err);
      }
    );
  }

  markAsDone() {
    this.visitService.markVisitAsDone(this.visit.id).subscribe(
      doneVisit => {
        this.visit = doneVisit;
        console.log(doneVisit);
      }, err => {
        console.log(err);
      }
    );
  }

  cancelVisit() {
    this.visitService.cancelVisit(this.visit.id).subscribe(
        cancelledVisit => {
          this.visit = cancelledVisit;
          console.log(cancelledVisit);
        }, err => {
          console.log(err);
        }
    );;
  }

  markAsPaid() {
    this.visitService.markVisitAsPaid(this.visit.id).subscribe(
        paidVisit => {
          this.visit = paidVisit;
          console.log(paidVisit);
        }, err => {
          console.log(err);
        }
    );;
  }

  editVisitTerm(){

    const termDialog = this.dialog.open(EditVisitTermComponent, {
      width: '500px',
      panelClass: 'my-dialog',
      data: { visitId: this.visit.id,}
    });

    termDialog.afterClosed().subscribe(
      result => {
        if(result.event == 'TERM_CHANGED'){
          this.dialogRef.close({
            event: 'TERM_CHANGED'
          });
        }
      }

    )
  }
}
