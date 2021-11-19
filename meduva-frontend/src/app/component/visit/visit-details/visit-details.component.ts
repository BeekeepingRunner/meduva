import { Component, OnInit } from '@angular/core';
import {Visit} from "../../../model/visit";
import {VisitService} from "../../../service/visit.service";
import {RoleGuardService} from "../../../service/auth/role-guard.service";
import {ActivatedRoute} from "@angular/router";
import {roleNames, UserRole} from "../../../model/user";

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

  constructor(
    private activatedRoute: ActivatedRoute,
    private visitService: VisitService,
    private roleGuard: RoleGuardService,
  ) { }

  ngOnInit(): void {
    let visitId = this.activatedRoute.snapshot.params.id;
    this.isWorker = this.roleGuard.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_WORKER]);
    this.isReceptionist = this.roleGuard.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_RECEPTIONIST]);
    if (visitId) {
      this.getVisitDetails(visitId);
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
}
