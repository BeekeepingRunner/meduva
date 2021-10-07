import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css']
})
export class PickTermComponent implements OnInit {

  availableTerms: Term[] = [];
  displayedColumns: string[] = ["date", "room", "item"];

  constructor(
    private visitService: VisitService,
    private datePipe: DatePipe,
  ) { }

  ngOnInit(): void {
    this.availableTerms = this.visitService.getAvailTerms();

    this.availableTerms.forEach((term: Term) => {
      let parsedDate = this.datePipe.transform(new Date(term.startTime), 'yyyy-MM-dd hh:mm');
      if (parsedDate != null) {
        term.startTime = parsedDate;
      }
    });

    console.log(this.availableTerms);
  }
}
