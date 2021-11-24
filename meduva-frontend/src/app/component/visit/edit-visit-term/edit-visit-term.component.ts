import {Component, Inject, OnInit} from '@angular/core';
import {User} from "../../../model/user";
import {Client} from "../../../model/client";
import {Service} from "../../../model/service";
import {Term, VisitService} from "../../../service/visit.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {DayDialogData} from "../../../schedule/component/dialog/day-dialog/day-dialog.component";
import {Visit} from "../../../model/visit";

export interface EditVisitTermData {
  visitId: number;
}

@Component({
  selector: 'app-edit-visit-term',
  templateUrl: './edit-visit-term.component.html',
  styleUrls: ['./edit-visit-term.component.css']
})
export class EditVisitTermComponent implements OnInit {

  worker!: User | null;
  client!: Client;
  service!: Service;
  visit!: Visit;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: EditVisitTermData,
    private visitService: VisitService,
  ) { }

  ngOnInit(): void {

    this.visitService.getVisitById(this.data.visitId).subscribe(
      data => {
        console.log(data);
        this.visit = data;
        this.service = this.visit.service;
        console.log(this.visit);
      }
    )



  }

}
