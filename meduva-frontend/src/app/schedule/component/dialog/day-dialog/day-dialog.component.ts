import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DayDialogData {
  day: Date
}

@Component({
  selector: 'app-day-dialog',
  templateUrl: './day-dialog.component.html',
  styleUrls: ['./day-dialog.component.css']
})
export class DayDialogComponent implements OnInit {

  dayDate: string = '';
  settingWorkHours: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<DayDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DayDialogData
  ) { }

  ngOnInit(): void {
    this.dayDate = this.data.day.toDateString();
  }
}
