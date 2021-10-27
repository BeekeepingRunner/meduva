import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {DayDialogComponent} from "../../dialog/day-dialog/day-dialog.component";
import {User} from "../../../../model/user";
import {UserService} from "../../../../service/user.service";
import {ScheduleService, WeekBoundaries, WorkHours} from "../../../service/schedule.service";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-worker-schedule',
  changeDetection: ChangeDetectionStrategy.Default, // if strange bugs will occur, try using .OnPush
  templateUrl: './worker-schedule.component.html',
  styleUrls: ['./worker-schedule.component.css']
})
export class WorkerScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  worker!: User;

  firstDayOfWeek!: Date;
  lastDayOfWeek!: Date;

  constructor(
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private userService: UserService,
    private scheduleService: ScheduleService,
    ) {
  }

  ngOnInit(): void {
    let workerId = this.activatedRoute.snapshot.params.id;
    this.userService.getUserDetails(workerId).subscribe(
      worker => {
        this.worker = worker;
        this.prepareWeekEvents();
      }
    );
  }

  openDayDialog(date: Date) {
    this.clickedDate = date;
    const dayDialog = this.dialog.open(DayDialogComponent, {
      width: '400px',
      panelClass: 'my-dialog',
      data: { date: this.clickedDate }
    });

    dayDialog.afterClosed().subscribe(
      result => {
        if (result.event == 'WORK_HOURS') {
          let workHoursToSave: WorkHours = result.data;
          console.log(workHoursToSave);
          this.scheduleService.saveWorkHours(this.worker.id, workHoursToSave).subscribe(
            workHours => {
              console.log(workHours);
            }, err => {
              console.log(err);
            }
          );
        }
      }
    );
  }

  prepareWeekEvents() {
    this.events = [];
    this.setFirstAndLastDayOfWeek();
    this.prepareWeekWorkHours();
  }

  setFirstAndLastDayOfWeek() {
    let currDate = new Date(this.viewDate);
    let firstDayOfWeekNumber = currDate.getDate() - currDate.getDay();
    let lastDayOfWeekNumber = firstDayOfWeekNumber + 6;
    this.firstDayOfWeek = new Date(currDate.setDate(firstDayOfWeekNumber));
    this.lastDayOfWeek = new Date(currDate.setDate(lastDayOfWeekNumber));
  }

  prepareWeekWorkHours(): void {
    let weekBoundaries: WeekBoundaries = {
      firstWeekDay: this.firstDayOfWeek,
      lastWeekDay: this.lastDayOfWeek
    };

    this.scheduleService.getWeekWorkHours(this.worker.id, weekBoundaries).subscribe(
    (weeklyWorkHours: WorkHours[]) => {
      let newEvents = this.events;
      this.events = [];
      weeklyWorkHours.forEach(workHours => {
        newEvents.push({
          draggable: false,
          end: new Date(workHours.endTime),
          id: undefined,
          meta: undefined,
          start: new Date(workHours.startTime),
          title: "Off work",
          color: {
            primary: "gray",
            secondary: "gray"
          }
        })
      });
      this.events = [...newEvents];
    });
  }

  /*
  private getWeeklyWorkHourEvents(weekWorkHours: WorkHours[]): Observable<CalendarEvent[]> {

    let workHoursEvents: CalendarEvent[] = [];
    weekWorkHours.forEach(workHours => {

      let dayStart: Date = new Date(workHours.startTime);
      dayStart.setHours(0);
      dayStart.setMinutes(0);
      dayStart.setSeconds(0);
      let dayEnd: Date = new Date(workHours.startTime);
      dayEnd.setHours(23);
      dayEnd.setMinutes(59);
      dayEnd.setSeconds(59);

      console.log(new Date(dayStart));
      console.log(new Date(workHours.startTime));
      console.log(new Date(workHours.endTime));
      console.log(new Date(dayEnd));

      // pre work off-time
      workHoursEvents.push({
        draggable: false,
        end: new Date(workHours.startTime),
        id: undefined,
        meta: undefined,
        start: new Date(dayStart),
        title: "Off work",
        color: {
          primary: "gray",
          secondary: "gray"
        }
      });

      // post work off-time
      workHoursEvents.push({
        draggable: false,
        end: new Date(dayEnd),
        id: undefined,
        meta: undefined,
        start: new Date(workHours.endTime),
        title: "Off work",
        color: {
          primary: "gray",
          secondary: "gray"
        }
      });
    });

    return new Observable<CalendarEvent[]>(workHoursEvents);
  }

   */

  eventClick($event: { event: CalendarEvent<any>; sourceEvent: any }) {

  }
}
