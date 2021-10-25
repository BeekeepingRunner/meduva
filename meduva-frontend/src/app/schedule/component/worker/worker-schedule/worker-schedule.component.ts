import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {DayDialogComponent} from "../../dialog/day-dialog/day-dialog.component";
import {User} from "../../../../model/user";
import {UserService} from "../../../../service/user.service";
import {ScheduleService, WorkHours} from "../../../service/schedule.service";

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

  printCurrDate() {
    console.log(this.viewDate);
  }
}
