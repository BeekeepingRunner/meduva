import { Component, OnInit } from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {Room} from "../../../../model/room";
import {RoomService} from "../../../../service/room.service";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EquipmentItem} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";

@Component({
  selector: 'app-item-schedule',
  templateUrl: './item-schedule.component.html',
  styleUrls: ['./item-schedule.component.css']
})
export class ItemScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  item!: EquipmentItem;

  firstDayOfWeek!: Date;
  lastDayOfWeek!: Date;

  dayStartHour: number = 6;
  dayEndHour: number = 20;

  constructor(
    private itemService: EquipmentService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    let itemId = this.activatedRoute.snapshot.params.id;
    this.itemService.getItemById(itemId).subscribe(
      item => {
        this.item = item;
      }
    );
  }

  openDayDialog(date: Date) {

  }

  eventClick($event: {event: CalendarEvent<any>; sourceEvent: any}) {

  }
}
