import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";
import {MatCalendarCellClassFunction} from "@angular/material/datepicker";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class PickTermComponent implements OnInit {

  selectedService!: Service | null;
  canChooseTerm: boolean = false;

  generatingTerms: boolean = false;
  canSelectHour: boolean = false;

  availableTerms: Term[] = [];
  displayedColumns: string[] = ["date"];

  errorMessage: string = '';

  dateClass: MatCalendarCellClassFunction<Date> = (cellDate, view) => {
    if (view === 'month') {
      let hasTerms: boolean = true;
      // check if service can be performed given day
      return hasTerms ? 'free-date' : 'not-available-date';
    }
    return '';
  }

  constructor(
    private visitService: VisitService,
    private servicesService: ServicesService,
    private datePipe: DatePipe,
    private router: Router,
  ) { }

  ngOnInit(): void {
    if (this.serviceHasBeenSelected()) {
      this.canChooseTerm = true;
    }
  }

  private serviceHasBeenSelected(): boolean {
    this.selectedService = this.visitService.getSelectedService();
    return this.selectedService != null && this.selectedService.id != null;
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
