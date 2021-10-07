import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css']
})
export class PickTermComponent implements OnInit {

  availableTerms: Term[] = [];

  constructor(
    private visitService: VisitService
  ) { }

  ngOnInit(): void {
    this.availableTerms = this.visitService.getAvailTerms();
    console.log(this.availableTerms);
  }
}
