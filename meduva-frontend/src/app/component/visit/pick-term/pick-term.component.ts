import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css']
})
export class PickTermComponent implements OnInit {

  availableTerms: Term[] = [];
  displayedColumns: string[] = ["date"];

  constructor(
    private visitService: VisitService,
    private datePipe: DatePipe,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.availableTerms = this.visitService.getAvailTerms();
  }

  selectTerm(term: Term) {
    console.log(term);
    this.visitService.saveSelectedTerm(term);
    this.router.navigate(['/visit/pick-client']);
  }
}
