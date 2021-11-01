import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {startTimeBeforeEndTimeValidator} from "../../../util/validator/hours-input";
import {WorkHours} from "../../../service/schedule.service";

export interface DayDialogData {
  date: Date
}

@Component({
  selector: 'app-day-dialog',
  templateUrl: './day-dialog.component.html',
  styleUrls: ['./day-dialog.component.css']
})
export class DayDialogComponent implements OnInit {

  selectedDate!: Date;
  dateString: string = '';

  settingWorkHours: boolean = false;
  settingAbsenceHours: boolean = false;
  form!: FormGroup;

  workHours!: WorkHours;

  constructor(
    public dialogRef: MatDialogRef<DayDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DayDialogData,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.selectedDate = this.data.date;
    this.dateString = this.data.date.toDateString();
  }

  startSettingWorkHours() {
    this.settingWorkHours = true;
    this.form = this.formBuilder.group({
      startTime : new FormControl('', [Validators.required]),
      endTime: new FormControl('', [Validators.required])
    }, { validators: startTimeBeforeEndTimeValidator });
  }

  onWorkHoursSave() {
    let startTime: Date = new Date(this.selectedDate);
    let endTime: Date = new Date(this.selectedDate);

    let hourAndMinutes: string = this.form.get('startTime')?.value; // HH:MM
    startTime = this.setHoursAndMinutes(startTime, hourAndMinutes);
    hourAndMinutes = this.form.get('endTime')?.value;
    endTime = this.setHoursAndMinutes(endTime, hourAndMinutes);

    this.workHours = {
      startTime: startTime,
      endTime: endTime
    }
    this.dialogRef.close({
      event: 'WORK_HOURS',
      data: this.workHours
    });
  }

  startSettingAbsenceHours(){
    this.settingAbsenceHours = true;
    this.form = this.formBuilder.group({
      startTime : new FormControl('', [Validators.required]),
      endTime: new FormControl('', [Validators.required]),
    }, { validators: startTimeBeforeEndTimeValidator });
  }

  private setHoursAndMinutes(dateTime: Date, hourAndMinutes: string): Date {
    let temp: string[] = hourAndMinutes.split(':');
    let hour = Number(temp[0]);
    let minutes = Number(temp[1]);

    dateTime.setHours(hour);
    dateTime.setMinutes(minutes);
    return dateTime;
  }

  setWholeDay(){
    this.form.get('startTime')?.setValue('06:00');
    this.form.get('endTime')?.setValue('20:00');
  }
}
