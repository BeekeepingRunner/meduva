import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {Client} from "../../../model/client";

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {

  term!: Term | null;
  client!: Client | null;

  constructor(
    private visitService: VisitService,
  ) { }

  ngOnInit(): void {
    this.term = this.visitService.getSelectedTerm();
    this.client = this.visitService.getSelectedClient();
    console.log(this.term);
    console.log(this.client);
  }

  submitVisit(): void {
    // this.visitService.saveVisit();
  }
}
