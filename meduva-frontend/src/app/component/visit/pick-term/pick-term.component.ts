import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Inject,
  OnDestroy,
  OnInit,
  ViewEncapsulation
} from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";
import {MatCalendar, MatDatepickerInputEvent} from "@angular/material/datepicker";
import {User} from "../../../model/user";
import {Subject} from "rxjs";
import {DateAdapter, MAT_DATE_FORMATS, MatDateFormats} from "@angular/material/core";
import {takeUntil} from "rxjs/operators";
import {addMonth, DateUtil, getFormattedDate, substractMonth} from "../../../util/date";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";

@Component({
  selector: 'app-pick-term',
  templateUrl: './pick-term.component.html',
  styleUrls: ['./pick-term.component.css'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.Default,
})
export class PickTermComponent implements OnInit {

  clientId!: number | undefined;
  selectedService!: Service | null;
  selectedWorker!: User | null;
  loading: boolean = true;
  canChooseDay: boolean = false;

  asyncHeader = AsyncDatePickerHeader;
  lastPickedDay!: Date | null;
  generatingTerms: boolean = false;
  canSelectTerm: boolean = false;

  availableTerms: Term[] = [];
  hourSelected: boolean = false;

  errorMessage: string = '';

  dateFilter = (date: Date | null): boolean => {
    if (date != null) {
      return this.isAvailable(date);
    } else {
      return false;
    }
  };

  dayChecker = (date: Date) => {
    if (date != null) {
      return this.isAvailable(date) ? 'free-date' : 'not-available-date';
    } else {
      return 'not-available-date';
    }
  }

  private isAvailable(date: Date): boolean {
    let isAvailable = false;
    let availableMonthDays: Date[] = this.visitService.getAvailableDates();
    console.log(availableMonthDays);
    if (availableMonthDays.length > 0) {
      let filteredDayNumber = date.getDate();
      availableMonthDays.forEach(availDate => {
        if (new Date(availDate).getDate() == filteredDayNumber) {
          isAvailable = true;
        }
      });
    }

    return isAvailable;
  }

  constructor(
    private visitService: VisitService,
    private servicesService: ServicesService,
    private jwtStorageService: JwtStorageService,
    private datePipe: DatePipe,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.clientId = this.jwtStorageService.getCurrentUser()?.id;
    this.selectedService = this.visitService.getSelectedService();
    if (this.serviceHasBeenSelected()) {
      console.log("service selected");
      this.selectedWorker = this.visitService.getSelectedWorker();
      if (this.workerHasBeenSelected()) {
        console.log("worker selected");
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
    let activeDateStr = getFormattedDate(this.getLastPickedDayOrNow());
    let serviceId = this.visitService.getSelectedService()?.id;
    let workerId = this.visitService.getSelectedWorker()?.id;
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(workerId, serviceId, activeDateStr).subscribe(
      availDays => {
        console.log("avail days " + availDays);
        this.visitService.saveAvailableDates(availDays);
        this.loading = false;
        this.canChooseDay = true;
      }, err => {
        console.log(err);
      }
    );
  }

  private getLastPickedDayOrNow(): Date {
    if (this.lastPickedDay != null) {
      return this.lastPickedDay;
    } else {
      return new Date();
    }
  }

  private waitForAvailableDays() {
    let activeDateStr = getFormattedDate(new Date());
    let serviceId = this.visitService.getSelectedService()?.id;
    // @ts-ignore
    this.visitService.getAvailableDaysInMonth(serviceId, activeDateStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);
        this.canChooseDay = true;
      }
    );
  }

  onDatePickerClick() {
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

  selectTerm(term: Term) {
    this.router.navigate(['/visit/pick-client']);
  }

  onDayPick($event: MatDatepickerInputEvent<Date, Date | null>) {
    this.hourSelected = false;
    this.lastPickedDay = $event.value;
    this.asyncHeader.lastPickedDay = $event.value;
    this.generatingTerms = true;
    // @ts-ignore
    let dayStr = getFormattedDate(this.lastPickedDay);
    // @ts-ignore
    this.visitService.getWorkerTermsForDay(this.selectedWorker?.id, this.selectedService?.id, dayStr).subscribe(
      (possibleTerms: Term[]) => {
        possibleTerms.forEach(term => {
          // @ts-ignore
          term.clientId = this.clientId;
        });
        console.log(possibleTerms);
        this.availableTerms = possibleTerms;
        this.canSelectTerm = true;
        this.generatingTerms = false;
        }, err => {
        console.log(err);
      });
  }

  onTermClick(term: Term) {
    this.visitService.saveSelectedTerm(term);
    this.hourSelected = true;
  }

  onSubmit() {
    console.log(this.visitService.getSelectedTerm());
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

  static lastPickedDay: Date | null;

  static getLastPickedDayOrNow() {
    if (this.lastPickedDay != null) {
      return this.lastPickedDay;
    } else {
      return new Date();
    }
  }

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
