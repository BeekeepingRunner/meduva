import { Component, OnInit } from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {Room} from "../../../../model/room";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../../service/room.service";

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
  ) { }

  ngOnInit(): void {
    let roomId = this.activatedRoute.snapshot.params.id;
    this.roomService.getById(roomId).subscribe(
      room => {
        this.room = room;
      }
    );
  }

  openDayDialog(date: Date) {

  }

  eventClick($event: {event: CalendarEvent<any>; sourceEvent: any}) {

  }
}
