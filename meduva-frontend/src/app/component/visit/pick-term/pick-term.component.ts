import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css']
})
export class PickTermComponent implements OnInit {

  selectedService!: Service | null;
  availableTerms: Term[] = [];
  displayedColumns: string[] = ["date"];

  generatingTerms: boolean = true;
  errorMessage: string = '';

  constructor(
    private visitService: VisitService,
    private servicesService: ServicesService,
    private datePipe: DatePipe,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.selectedService = this.visitService.getSelectedService();
    if (this.selectedService != null && this.selectedService.id) {
      this.getTermsForService(this.selectedService.id);
    } else {
      // error handling?
    }
  }

  private getTermsForService(serviceId: number) {
    this.servicesService.getTermsForService(serviceId).subscribe(
      terms => {
        this.availableTerms = terms;
        this.generatingTerms = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.generatingTerms = false;
      }
    );
  }

  selectTerm(term: Term) {
    this.visitService.saveSelectedTerm(term);
    this.router.navigate(['/visit/pick-client']);
  }
}
