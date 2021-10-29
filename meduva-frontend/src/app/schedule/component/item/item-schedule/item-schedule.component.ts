import { Component, OnInit } from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EquipmentItem} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";
import {DayDialogComponent} from "../../dialog/day-dialog/day-dialog.component";
import {ScheduleService, WorkHours} from "../../../service/schedule.service";
import {ItemDayDialogComponent, UnavailabilityOptions} from "../../dialog/item-day-dialog/item-day-dialog.component";

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
    private scheduleService: ScheduleService
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
      this.scheduleService.setItemDayUnavailability(this.item.id, this.clickedDate).subscribe(
        data => {
          console.log(data);
        }
      )
    } else if (selectedOption == UnavailabilityOptions.THAT_DAY_AND_NEXT) {
      // ...
    }
  }

  eventClick($event: {event: CalendarEvent<any>; sourceEvent: any}) {

  }
}
