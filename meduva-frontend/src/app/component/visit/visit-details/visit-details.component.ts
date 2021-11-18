import { Component, OnInit } from '@angular/core';
import {Visit} from "../../../model/visit";
import {VisitService} from "../../../service/visit.service";
import {RoleGuardService} from "../../../service/auth/role-guard.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-visit-details',
  templateUrl: './visit-details.component.html',
  styleUrls: ['./visit-details.component.css']
})
export class VisitDetailsComponent implements OnInit {

  visit!: Visit;

  constructor(
    private activatedRoute: ActivatedRoute,
    private visitService: VisitService,
    private roleGuard: RoleGuardService,
  ) { }

  ngOnInit(): void {
    let visitId = this.activatedRoute.snapshot.params.id;
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
}
