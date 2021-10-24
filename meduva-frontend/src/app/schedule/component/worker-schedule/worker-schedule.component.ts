import {ChangeDetectionStrategy, Component, NgModule, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";

@Component({
  selector: 'app-worker-schedule',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './worker-schedule.component.html',
  styleUrls: ['./worker-schedule.component.css']
})
export class WorkerScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  ngOnInit(): void {
  }
}
