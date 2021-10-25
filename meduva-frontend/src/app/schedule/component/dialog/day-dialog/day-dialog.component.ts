import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {controlsConfig} from "../../../../component/clients/add-client/add-client.component";
import {startTimeBeforeEndTimeValidator} from "../../../util/validator/hours-input";

export interface DayDialogData {
  day: Date
}

export const START_TIME_IDX = 0;
export const END_TIME_IDX = 1;

@Component({
  selector: 'app-day-dialog',
  templateUrl: './day-dialog.component.html',
  styleUrls: ['./day-dialog.component.css']
})
export class DayDialogComponent implements OnInit {

  dayDate: string = '';

  settingWorkHours: boolean = false;
  form!: FormGroup;

  workHours: string[] = [];

  constructor(
    public dialogRef: MatDialogRef<DayDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DayDialogData,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.dayDate = this.data.day.toDateString();
  }

  startSettingWorkHours() {
    this.settingWorkHours = true;
    this.form = this.formBuilder.group({
      startTime : new FormControl('', [Validators.required]),
      endTime: new FormControl('', [Validators.required])
    }, { validators: startTimeBeforeEndTimeValidator });
  }

  onWorkHoursSave() {
    this.workHours[START_TIME_IDX] = this.form.get('startTime')?.value;
    this.workHours[END_TIME_IDX] = this.form.get('endTime')?.value;
    this.dialogRef.close({
      event: 'WORK_HOURS',
      data: this.workHours
    });
  }
}
