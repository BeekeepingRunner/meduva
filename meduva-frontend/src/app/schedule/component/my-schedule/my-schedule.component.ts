import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {User} from "../../../model/user";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../../service/user.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {ScheduleService, Visit, WeekBoundaries, WorkHours, WorkSchedule} from "../../service/schedule.service";
import {DayDialogComponent} from "../dialog/day-dialog/day-dialog.component";
import {
  createAbsenceHoursEvent,
  createOffWorkHoursEvent,
  createVisitsAsClientEvent,
  createVisitsAsWorkerEvent
} from "../../util/event/creation";
import {VisitDetailsComponent} from "../../../component/visit/visit-details/visit-details.component";

@Component({
  selector: 'app-my-schedule',
  changeDetection: ChangeDetectionStrategy.Default,
  templateUrl: './my-schedule.component.html',
  styleUrls: ['./my-schedule.component.css']
})
export class MyScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  worker!: User;

  weekBoundaries!: WeekBoundaries;
  firstDayOfWeek!: Date;
  lastDayOfWeek!: Date;

  dayStartHour: number = 6;
  dayEndHour: number = 20;

  constructor(
    private jwtStorageService: JwtStorageService,
    private dialog: MatDialog,
    private userService: UserService,
    private scheduleService: ScheduleService,
  ) {
  }

  ngOnInit(): void {
    let workerId = this.jwtStorageService.getCurrentUser()?.id;
    if (workerId) {
      this.userService.getUserDetails(workerId).subscribe(
        worker => {
          this.worker = worker;
          this.prepareWeekEvents();
        }
      );
    }
  }

  prepareWeekEvents() {
    this.events = [];
    this.setFirstAndLastDayOfWeek();
    this.prepareWeeklyOffWorkHours();
  }

  private setFirstAndLastDayOfWeek() {
    let currDate = new Date(this.viewDate);
    let firstDayOfWeekNumber = currDate.getDate() - currDate.getDay();
    this.firstDayOfWeek = new Date(currDate.setDate(firstDayOfWeekNumber));
    this.lastDayOfWeek = new Date(currDate.setDate(this.firstDayOfWeek.getDate() + 6));
  }

  private prepareWeeklyOffWorkHours(): void {
    this.weekBoundaries = {
      firstWeekDay: this.firstDayOfWeek,
      lastWeekDay: this.lastDayOfWeek
    };

    // @ts-ignore
    this.scheduleService.getWeeklyOffWorkHours(this.worker.id, this.weekBoundaries).subscribe(
      (weeklyOffWorkHours: WorkHours[]) => {
        this.updateWorkHoursEvents(weeklyOffWorkHours);
        this.prepareWeeklyAbsenceHours();
      });
  }

  private updateWorkHoursEvents(weeklyOffWorkHours: WorkHours[]) {
    let newEvents = this.events;
    this.events = [];
    weeklyOffWorkHours.forEach(offWorkHours => {
      newEvents.push(
        createOffWorkHoursEvent(offWorkHours.startTime, offWorkHours.endTime))
    });
    this.events = [...newEvents];
  }

  private prepareWeeklyAbsenceHours(): void {

    this.scheduleService.getWeeklyAbsenceHours(this.worker.id, this.weekBoundaries).subscribe(
      (weeklyAbsenceHours: WorkSchedule[]) => {
        console.log(weeklyAbsenceHours);
        this.updateAbsenceHoursEvents(weeklyAbsenceHours);
        this.prepareWeeklyVisitsAsWorker();
      }
    );
  }

  private prepareWeeklyVisitsAsWorker(): void {

    this.scheduleService.getWeeklyBookedVisitsAsWorker(this.worker.id, this.weekBoundaries).subscribe(
      /* possibly later change WorkSchedule on new interface (Visit?) */
      (weeklyVisitsAsWorker: Visit[]) => {
        console.log(weeklyVisitsAsWorker);
        this.updateVisitsAsWorkerEvents(weeklyVisitsAsWorker);
        this.prepareWeeklyVisitsAsClient();
      }
    );
  }

  private prepareWeeklyVisitsAsClient(): void {

    this.scheduleService.getWeeklyBookedVisitsAsClient(this.worker.id, this.weekBoundaries).subscribe(
      /* possibly later change WorkSchedule on new interface (Visit?) */
      (weeklyVisitsAsClient: Visit[]) => {
        console.log(weeklyVisitsAsClient);
        this.updateVisitsAsClientEvents(weeklyVisitsAsClient);
      }
    );
  }

  private updateVisitsAsWorkerEvents(weeklyVisitsAsWorker: Visit[]){
    let newEvents = this.events;
    this.events = [];
    weeklyVisitsAsWorker.forEach(visitAsWorker => {
      newEvents.push(
        createVisitsAsWorkerEvent(visitAsWorker.timeFrom, visitAsWorker.timeTo, visitAsWorker.id));
    });
    this.events = [...newEvents];
  }

  private updateVisitsAsClientEvents(weeklyVisitsAsClient: Visit[]) {
    let newEvents = this.events;
    this.events = [];
    weeklyVisitsAsClient.forEach(visitAsWorker => {
      newEvents.push(
        createVisitsAsClientEvent(visitAsWorker.timeFrom, visitAsWorker.timeTo, visitAsWorker.id));
    });
    this.events = [...newEvents];
  }

  private updateAbsenceHoursEvents(weeklyAbsenceHours: WorkSchedule[]) {
    let newEvents = this.events;
    this.events = [];
    weeklyAbsenceHours.forEach(absenceHours => {
      newEvents.push(
        createAbsenceHoursEvent(absenceHours.timeFrom, absenceHours.timeTo)
      );
    });
    this.events = [...newEvents];
  }

  openVisitDetailsDialog(id: string | number | undefined) {
    const visitDetailsDialog = this.dialog.open(VisitDetailsComponent, {
      width: '600px',
      height: '600px',
      panelClass: 'my-dialog',
      data: {
        visitId: id,
      }
    });

    visitDetailsDialog.afterClosed().subscribe(
      (value => {
        this.prepareWeekEvents();
      })
    );
  }

  eventClick($event: { event: CalendarEvent<any>; sourceEvent: any }) {

    if($event.event.id != null){
      this.openVisitDetailsDialog($event.event.id);
    }
  }

  openDayDialog(date: Date){
      ///?
  }

}
