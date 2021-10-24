import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CalendarEvent, CalendarView} from "angular-calendar";
import {User} from "../../../model/user";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../../service/user.service";
import {JwtTokenStorageService, TokenUserInfo} from "../../../service/token/jwt-token-storage.service";

@Component({
  selector: 'app-my-schedule',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './my-schedule.component.html',
  styleUrls: ['./my-schedule.component.css']
})
export class MyScheduleComponent implements OnInit {

  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [];

  clickedDate!: Date;
  clickedColumn!: number;

  currentUser!: TokenUserInfo | null;

  constructor(
    private jwtTokenStorageService: JwtTokenStorageService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private userService: UserService,
  ) {
  }

  ngOnInit(): void {
    this.currentUser = this.jwtTokenStorageService.getCurrentUser();
  }

  openDayDialog(date: Date) {
    
  }
}
