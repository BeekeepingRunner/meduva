import { Component, OnInit } from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EquipmentItem} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";
import {ScheduleService, TimeRange, WeekBoundaries, WorkSchedule} from "../../../service/schedule.service";
import {ItemDayDialogComponent, UnavailabilityOptions} from "../../dialog/item-day-dialog/item-day-dialog.component";
import {createUnavailabilityEvent} from "../../../util/event/creation";
import {MatSnackBar} from "@angular/material/snack-bar";

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
    private scheduleService: ScheduleService,
    public snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    let itemId = this.activatedRoute.snapshot.params.id;
    this.itemService.getItemById(itemId).subscribe(
      item => {
        this.item = item;
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
    this.scheduleService.getWeeklyItemUnavailability(this.item.id, weekBoundaries).subscribe(
      (weeklyUnavailability: TimeRange[]) => {
        this.pushUnavailabilities(weeklyUnavailability);
        this.pushWeeklyVisits();
      });
  }

  private pushUnavailabilities(weeklyUnavailability: TimeRange[]) {
    let newEvents = this.events;
    weeklyUnavailability.forEach(unavailability => {
      newEvents.push(
        createUnavailabilityEvent(unavailability.startTime, unavailability.endTime)
      );
    });
    this.events = [];
    this.events = [...newEvents];
  }

  private pushWeeklyVisits(): void {
    let weekBoundaries: WeekBoundaries = {
      firstWeekDay: this.firstDayOfWeek,
      lastWeekDay: this.lastDayOfWeek,
    }

    this.scheduleService.getWeeklyItemVisits(this.item.id, weekBoundaries).subscribe(
      (weeklyItemVisits: WorkSchedule[]) => {
        console.log(weeklyItemVisits);
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

        switch(result.event) {
          case 'UNAVAILABILITY_SET':
              let selectedOption: number = result.data;
              this.setUnavailability(selectedOption);
              break;
          case 'UNAVAILABILITY_DELETE':
              this.checkIfUnavailabilityExists();
              break;
        }
      }
    );
  }

  private setUnavailability(selectedOption: number) {

    if (selectedOption == UnavailabilityOptions.THAT_DAY) {
      // @ts-ignore
      this.scheduleService.setItemDayUnavailability(this.item.id, this.clickedDate).subscribe(
        (dayTimeRange: TimeRange) => {
          console.log(dayTimeRange); // git gud
          this.pushUnavailableDayToEvents(dayTimeRange);
          this.getWeeklyEvents();
        }
      )
    }
  }

  private checkIfUnavailabilityExists() {
    let dayBoundaries: TimeRange = {
      startTime: this.clickedDate,
      endTime: this.clickedDate
    };
    let unavailabilityExists: boolean = false;

    // @ts-ignore
    this.scheduleService.getWeeklyItemUnavailability(this.item.id, dayBoundaries).subscribe(
      (weeklyUnavailability: TimeRange[]) => {
        (weeklyUnavailability.length != 0) ? unavailabilityExists = true : unavailabilityExists = false;

        if(!unavailabilityExists){
          this.snackBar.open('Unavailability does not exist!');
        } else {
          this.deleteDailyUnavailability();
        }
      });
  }

  private deleteDailyUnavailability() {
      this.scheduleService.deleteDailyItemUnavailability(this.item.id, this.clickedDate).subscribe(
        data => {
          this.snackBar.open('Unavailability deleted!');
          this.getWeeklyEvents();
        }
      );
  }

  private pushUnavailableDayToEvents(dayTimeRange: TimeRange) {
    let newEvents = this.events;
    newEvents.push({
      draggable: false,
      end: new Date(dayTimeRange.endTime),
      id: undefined,
      meta: undefined,
      start: new Date(dayTimeRange.startTime),
      title: "Unavailable",
      color: {
        primary: "#FF9191",
        secondary: "#FF9191"
      }
    });
    this.events = [];
    this.events = [...newEvents];
  }

  eventClick($event: {event: CalendarEvent<any>; sourceEvent: any}) {

  }

}
