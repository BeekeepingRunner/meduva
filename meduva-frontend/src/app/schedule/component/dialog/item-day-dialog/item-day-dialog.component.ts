import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DayDialogData} from "../day-dialog/day-dialog.component";

export enum UnavailabilityOptions {
  THAT_DAY,
  THAT_DAY_AND_NEXT
}

@Component({
  selector: 'app-day-dialog',
  templateUrl: './item-day-dialog.component.html',
  styleUrls: ['./item-day-dialog.component.css']
})
export class ItemDayDialogComponent implements OnInit {

  selectedDate!: Date;
  dateString: string = '';

  settingUnavailability: boolean = false;
  form!: FormGroup;

  THAT_DAY: number = 0;
  THAT_DAY_AND_NEXT: number = 1;

  selectedUnavailOption!: number;

  constructor(
    public dialogRef: MatDialogRef<ItemDayDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DayDialogData,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.selectedDate = this.data.date;
    this.dateString = this.data.date.toDateString();
  }

  onUnavailabilitySet() {
    this.dialogRef.close({
      event: 'UNAVAILABILITY_SET',
      data: this.selectedUnavailOption
    })
  }
  /*
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

  private setHoursAndMinutes(dateTime: Date, hourAndMinutes: string): Date {
    let temp: string[] = hourAndMinutes.split(':');
    let hour = Number(temp[0]);
    let minutes = Number(temp[1]);

    dateTime.setHours(hour);
    dateTime.setMinutes(minutes);
    return dateTime;
  }

   */

}
