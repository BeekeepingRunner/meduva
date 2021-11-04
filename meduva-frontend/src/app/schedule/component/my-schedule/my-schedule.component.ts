import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {User} from "../../../model/user";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../../service/user.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {ScheduleService, WeekBoundaries, WorkHours} from "../../service/schedule.service";
import {DayDialogComponent} from "../dialog/day-dialog/day-dialog.component";

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
    let weekBoundaries: WeekBoundaries = {
      firstWeekDay: this.firstDayOfWeek,
      lastWeekDay: this.lastDayOfWeek
    };

    // @ts-ignore
    this.scheduleService.getWeeklyOffWorkHours(this.worker.id, weekBoundaries).subscribe(
      (weeklyOffWorkHours: WorkHours[]) => {
        this.updateWorkHoursEvents(weeklyOffWorkHours);
      });
  }

  private updateWorkHoursEvents(weeklyOffWorkHours: WorkHours[]) {
    let newEvents = this.events;
    this.events = [];
    weeklyOffWorkHours.forEach(OffWorkHours => {
      newEvents.push({
        draggable: false,
        end: new Date(OffWorkHours.endTime),
        id: undefined,
        meta: undefined,
        start: new Date(OffWorkHours.startTime),
        title: "Off work",
        color: {
          primary: "lightGray",
          secondary: "lightGray"
        }
      })
    });
    this.events = [...newEvents];
  }

  openDayDialog(dayDate: Date) {

  }

  eventClick($event: { event: CalendarEvent<any>; sourceEvent: any }) {

  }
}
