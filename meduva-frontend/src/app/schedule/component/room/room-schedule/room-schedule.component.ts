import { Component, OnInit } from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {Room} from "../../../../model/room";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../../service/room.service";
import {ItemDayDialogComponent, UnavailabilityOptions} from "../../dialog/item-day-dialog/item-day-dialog.component";
import {ScheduleService, TimeRange} from "../../../service/schedule.service";
import {createUnavailabilityEvent} from "../../../util/event/creation";

@Component({
  selector: 'app-room-schedule',
  templateUrl: './room-schedule.component.html',
  styleUrls: ['./room-schedule.component.css']
})
export class RoomScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  room!: Room;

  firstDayOfWeek!: Date;
  lastDayOfWeek!: Date;

  dayStartHour: number = 6;
  dayEndHour: number = 20;

  constructor(
    private roomService: RoomService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private scheduleService: ScheduleService
  ) { }

  ngOnInit(): void {
    let roomId = this.activatedRoute.snapshot.params.id;
    this.roomService.getById(roomId).subscribe(
      room => {
        this.room = room;
        this.getWeeklyEvents();
      }
    );
  }

  public getWeeklyEvents() {
    this.events = [];
    this.setFirstAndLastDayOfWeek();
    this.pushWeeklyUnavailability();
  }

  private setFirstAndLastDayOfWeek() {
    let currDate = new Date(this.viewDate);
    let firstDayOfWeekNumber = currDate.getDate() - currDate.getDay();
    this.firstDayOfWeek = new Date(currDate.setDate(firstDayOfWeekNumber));
    this.lastDayOfWeek = new Date(currDate.setDate(this.firstDayOfWeek.getDate() + 6));
  }

  private pushWeeklyUnavailability(): void {
    let weekBoundaries: TimeRange = {
      startTime: this.firstDayOfWeek,
      endTime: this.lastDayOfWeek
    };

    // @ts-ignore
    this.scheduleService.getWeeklyRoomUnavailability(this.room.id, weekBoundaries).subscribe(
      (weeklyUnavailability: TimeRange[]) => {
        this.pushUnavailabilities(weeklyUnavailability);
      });
  }

  private pushUnavailabilities(weeklyUnavailability: TimeRange[]) {
    let newEvents = this.events;
    this.events = [];
    weeklyUnavailability.forEach(unavailability => {
      newEvents.push(
        createUnavailabilityEvent(unavailability.startTime, unavailability.endTime)
      );
    });
    this.events = [...newEvents];
  }

  openDayDialog(date: Date) {
    this.clickedDate = date;
    const dayDialog = this.dialog.open(ItemDayDialogComponent, {
      width: '400px',
      panelClass: 'my-dialog',
      data: { date: this.clickedDate }
    });

    dayDialog.afterClosed().subscribe(
      result => {
        if (result.event == 'UNAVAILABILITY_SET') {
          let selectedOption: number = result.data;
          this.setUnavailability(selectedOption);
        }
      }
    );
  }

  private setUnavailability(selectedOption: number) {

    if (selectedOption == UnavailabilityOptions.THAT_DAY) {
      // @ts-ignore
      this.scheduleService.setRoomDayUnavailability(this.room.id, this.clickedDate).subscribe(
        (dayTimeRange: TimeRange) => {
          this.pushUnavailableDayToEvents(dayTimeRange);
          this.getWeeklyEvents();
        }
      )
    }
  }

  private pushUnavailableDayToEvents(dayTimeRange: TimeRange) {
    let newEvents = this.events;
    newEvents.push(
      createUnavailabilityEvent(dayTimeRange.startTime, dayTimeRange.endTime));
    this.events = [];
    this.events = [...newEvents];
  }

  eventClick($event: {event: CalendarEvent<any>; sourceEvent: any}) {

  }
}
