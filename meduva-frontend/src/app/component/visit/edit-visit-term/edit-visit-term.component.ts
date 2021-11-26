import {Component, Inject, OnInit} from '@angular/core';
import {User} from "../../../model/user";
import {Client} from "../../../model/client";
import {Service} from "../../../model/service";
import {Term, VisitService} from "../../../service/visit.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DayDialogData} from "../../../schedule/component/dialog/day-dialog/day-dialog.component";
import {Visit} from "../../../model/visit";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";

export interface EditVisitTermData {
  visitId: number;
}

@Component({
  selector: 'app-edit-visit-term',
  templateUrl: './edit-visit-term.component.html',
  styleUrls: ['./edit-visit-term.component.css']
})
export class EditVisitTermComponent implements OnInit {

  worker!: User;
  client!: Client;
  service!: Service;
  visit!: Visit;
  term!: Term;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: EditVisitTermData,
    private visitService: VisitService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<EditVisitTermComponent>,
  ) { }

  ngOnInit(): void {

    this.visitService.getVisitById(this.data.visitId).subscribe(
      data => {
        this.visit = data;
        this.service = this.visit.service;
        this.visit.userVisits.forEach( item => {
            if(item.asClient){
              this.client = item.user;
            } else {
              this.worker = item.user;
            }
          }
        );
      }
    );
  }

  onTermSelection($event: Term) {
    this.term = $event;
  }

  onVisitSubmit(){
    if(this.visit.paid){
      this.term.paid=true;
    }

    this.visitService.saveVisit(this.term).subscribe(
      visitData => {
          this.visitService.markVisitAsDeleted(this.visit.id).subscribe(
            response => {
                this.dialogRef.close({
                    event: 'TERM_CHANGED'
                  }
                );
            }
          )
      }, err => {
        console.log(err);
      }
    );
  }

}
