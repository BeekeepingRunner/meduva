import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component, EventEmitter,
  Inject,
  OnDestroy,
  OnInit, Output,
  ViewEncapsulation
} from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe, formatDate} from "@angular/common";
import {Router} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";
import {MatCalendar} from "@angular/material/datepicker";
import {User} from "../../../model/user";
import {Subject} from "rxjs";
import {DateAdapter, MAT_DATE_FORMATS, MatDateFormats} from "@angular/material/core";
import {takeUntil} from "rxjs/operators";
import {addMonth, DateUtil, getFormattedDate, substractMonth} from "../../../util/date";
import {locale} from "moment";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.Default,
})
export class PickTermComponent implements OnInit {

  selectedService!: Service | null;
  selectedWorker!: User | null;
  canChooseTerm: boolean = false;

  generatingTerms: boolean = false;
  canSelectHour: boolean = false;

  availableTerms: Term[] = [];
  displayedColumns: string[] = ["date"];

  errorMessage: string = '';

  asyncHeader = AsyncDatePickerHeader;

  dateFilter = (date: Date | null): boolean => {
    let availableDates = this.visitService.getAvailableDates();
    console.log(availableDates);

    /*
    const filteredDayNumber = (date || new Date()).getDate();
    let isAvailable = false;
    availableDates.forEach(availDate => {
      if (availDate.getDate() == filteredDayNumber)
        isAvailable = true;
    });
    return isAvailable;*/
    return true;
    /*
    if (date != null) {
      if (isMonthSame(date, this.openDates[0])) {
        return this.openDates.includes(date);
      } else {
        return false;
      }
    } else {
      return false;
    }
     */
  };

  constructor(
    private visitService: VisitService,
    private servicesService: ServicesService,
    private datePipe: DatePipe,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.selectedService = this.visitService.getSelectedService();
    if (this.serviceHasBeenSelected()) {

      this.selectedWorker = this.visitService.getSelectedWorker();
      if (this.workerHasBeenSelected()) {
        this.waitForWorkerAvailableDays();
      } else {
        this.waitForAvailableDays();
      }
    }
  }

  private serviceHasBeenSelected(): boolean {
    return this.selectedService != null && this.selectedService.id != null;
  }

  private workerHasBeenSelected(): boolean {
    return this.selectedWorker != null && this.selectedWorker.id != null;
  }

  private waitForWorkerAvailableDays() {
    let activeDateStr = getFormattedDate(new Date());
    let serviceId = this.visitService.getSelectedService()?.id;
    let workerId = this.visitService.getSelectedWorker()?.id;
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(workerId, serviceId, activeDateStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);
        this.canChooseTerm = true;
      }
    );
  }

  private waitForAvailableDays() {
    let activeDateStr = getFormattedDate(new Date());
    let serviceId = this.visitService.getSelectedService()?.id;
    // @ts-ignore
    this.visitService.getAvailableDaysInMonth(serviceId, activeDateStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);
        this.canChooseTerm = true;
      }
    );
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

/** Custom header component for datepicker. */
@Component({
  selector: 'async-header',
  styles: [`
    .example-header {
      display: flex;
      align-items: center;
      padding: 0.5em;
    }

    .example-header-label {
      flex: 1;
      height: 1em;
      font-weight: 500;
      text-align: center;
    }

    .example-double-arrow .mat-icon {
      margin: -22%;
    }
  `],
  template: `
    <div class="example-header">
      <button mat-icon-button (click)="previousClicked('month')">
        <mat-icon>keyboard_arrow_left</mat-icon>
      </button>
      <span class="example-header-label">{{periodLabel}}</span>
      <button mat-icon-button (click)="nextClicked('month')">
        <mat-icon>keyboard_arrow_right</mat-icon>
      </button>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AsyncDatePickerHeader<D> implements OnInit, OnDestroy {
  private _destroyed = new Subject<void>();

  workerId!: number | undefined;
  serviceId!: number | undefined;
  availableDays: Date[] = [];

  constructor(
    private _calendar: MatCalendar<D>, private _dateAdapter: DateAdapter<D>,
    @Inject(MAT_DATE_FORMATS) private _dateFormats: MatDateFormats, cdr: ChangeDetectorRef,
    private visitService: VisitService,
    private dateUtil: DateUtil
    ) {
    _calendar.stateChanges
      .pipe(takeUntil(this._destroyed))
      .subscribe(() => cdr.markForCheck());
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete();
  }

  get periodLabel() {
    return this._dateAdapter
      .format(this._calendar.activeDate, this._dateFormats.display.monthYearLabel)
      .toLocaleUpperCase();
  }

  previousClicked(mode: 'month' | 'year') {
    // @ts-ignore
    let dateToSend = new Date(this._calendar.activeDate);
    dateToSend = substractMonth(dateToSend);
    let dateToSendStr = getFormattedDate(dateToSend);

    this.serviceId = this.visitService.getSelectedService()?.id;
    this.workerId = this.visitService.getSelectedWorker()?.id;
    if (this.workerId != undefined) {
      this.waitForWorkerPreviousAvailableDays(mode, dateToSendStr);
    } else {
      // TODO: check if service can be performed given day by anyone
      // this.visitService.getAvailableDaysInMonth(serviceId, activeDate);
    }
  }

  private waitForWorkerPreviousAvailableDays(mode: 'month' | 'year', monthDayStr: string) {
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(this.workerId, this.serviceId, monthDayStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);

        this._calendar.activeDate = mode === 'month' ?
          this._dateAdapter.addCalendarMonths(this._calendar.activeDate, -1) :
          this._dateAdapter.addCalendarYears(this._calendar.activeDate, -1);
      }
    );
  }

  nextClicked(mode: 'month' | 'year') {
    // @ts-ignore
    let dateToSend = new Date(this._calendar.activeDate);
    dateToSend = addMonth(dateToSend);
    let dateToSendStr = getFormattedDate(dateToSend);

    this.serviceId = this.visitService.getSelectedService()?.id;
    this.workerId = this.visitService.getSelectedWorker()?.id;
    if (this.workerId != undefined) {
      this.waitForWorkerNextAvailableDays(mode, dateToSendStr);
    } else {
      // TODO: check if service can be performed given day by anyone
      // this.visitService.getAvailableDaysInMonth(serviceId, activeDate);
    }
  }

  private waitForWorkerNextAvailableDays(mode: 'month' | 'year', monthDayStr: string) {
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(this.workerId, this.serviceId, monthDayStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);

        this._calendar.activeDate = mode === 'month' ?
          this._dateAdapter.addCalendarMonths(this._calendar.activeDate, 1) :
          this._dateAdapter.addCalendarYears(this._calendar.activeDate, 1);
      }
    );
  }
}
