import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component, EventEmitter,
  Inject, Input,
  OnDestroy,
  OnInit, Output,
  ViewEncapsulation
} from '@angular/core';
import {Service} from "../../../../model/service";
import {User} from "../../../../model/user";
import {Term, VisitService} from "../../../../service/visit.service";
import {addMonth, getFormattedDate, isInThePast, substractMonth} from "../../../../util/date";
import {ServicesService} from "../../../../service/services.service";
import {JwtStorageService} from "../../../../service/token/jwt-storage.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {MatCalendar, MatDatepickerInputEvent} from "@angular/material/datepicker";
import {Subject} from "rxjs";
import {DateAdapter, MAT_DATE_FORMATS, MatDateFormats} from "@angular/material/core";
import {takeUntil} from "rxjs/operators";
import {Client} from "../../../../model/client";

@Component({
  selector: 'app-term-selection',
  templateUrl: './term-selection.component.html',
  styleUrls: ['./term-selection.component.css'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.Default,
})
export class TermSelectionComponent implements OnInit {

  @Input()
  client!: Client;
  @Input()
  service!: Service;
  @Input()
  worker!: User;

  loading: boolean = true;
  canChooseDay: boolean = false;

  asyncHeader = AsyncDatePickerHeader;
  lastPickedDay!: Date | null;
  generatingTerms: boolean = false;
  canSelectTerm: boolean = false;

  availableTerms: Term[] = [];
  hourSelected: boolean = false;
  @Output()
  termEmitter = new EventEmitter<Term>();

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
    if (isInThePast(date)) {
      return false;
    }

    let isAvailable = false;
    let availableMonthDays: Date[] = this.visitService.getAvailableDates();
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
  ) { }

  ngOnInit(): void {
    AsyncDatePickerHeader.worker = this.worker;
    AsyncDatePickerHeader.service = this.service;
    this.waitForAvailableDays();
  }

  private waitForAvailableDays() {
    if (this.worker) {
      this.waitForWorkerAvailableDays();
    } else {
      this.waitForAnyonesAvailableDays();
    }
  }

  private waitForWorkerAvailableDays() {
    let activeDateStr = getFormattedDate(this.getLastPickedDayOrNow());
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(this.worker.id, this.service.id, activeDateStr).subscribe(
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

  private waitForAnyonesAvailableDays() {
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
    this.waitForAvailableDays();
  }

  onDayPick($event: MatDatepickerInputEvent<Date, Date | null>) {
    this.lastPickedDay = $event.value;
    this.asyncHeader.lastPickedDay = $event.value;

    this.hourSelected = false;
    this.generatingTerms = true;
    // @ts-ignore
    let dayStr = getFormattedDate(this.lastPickedDay);
    // @ts-ignore
    this.visitService.getWorkerTermsForDay(this.worker.id, this.service.id, dayStr).subscribe(
      (possibleTerms: Term[]) => {
        this.availableTerms = possibleTerms;
        this.canSelectTerm = true;
        this.generatingTerms = false;
      }, err => {
        console.log(err);
      });
  }

  onTermClick(term: Term) {
    term.clientId = this.client.id;
    if (this.client.email == null || this.client.email == undefined) {
      term.clientUnregistered = true;
    }
    console.log(term);
    this.hourSelected = true;
    this.termEmitter.emit(term);
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
  static worker: User;
  static service: Service;

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

    if (AsyncDatePickerHeader.worker) {
      this.waitForWorkerPreviousAvailableDays(mode, dateToSendStr);
    } else {
      // TODO: SOMEDAY - check if service can be performed given day by anyone
      // this.visitService.getAvailableDaysInMonth(serviceId, activeDate);
    }
  }

  private waitForWorkerPreviousAvailableDays(mode: 'month' | 'year', monthDayStr: string) {
    let workerId = AsyncDatePickerHeader.worker.id;
    let serviceId = AsyncDatePickerHeader.service.id;
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(workerId, serviceId, monthDayStr).subscribe(
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

    if (AsyncDatePickerHeader.worker) {
      this.waitForWorkerNextAvailableDays(mode, dateToSendStr);
    } else {
      // TODO: check if service can be performed given day by anyone
      // this.visitService.getAvailableDaysInMonth(serviceId, activeDate);
    }
  }

  private waitForWorkerNextAvailableDays(mode: 'month' | 'year', monthDayStr: string) {
    let workerId = AsyncDatePickerHeader.worker.id;
    let serviceId = AsyncDatePickerHeader.service.id;
    // @ts-ignore
    this.visitService.getWorkerAvailableDaysInMonth(workerId, serviceId, monthDayStr).subscribe(
      availDays => {
        this.visitService.saveAvailableDates(availDays);

        this._calendar.activeDate = mode === 'month' ?
          this._dateAdapter.addCalendarMonths(this._calendar.activeDate, 1) :
          this._dateAdapter.addCalendarYears(this._calendar.activeDate, 1);
      }
    );
  }
}
