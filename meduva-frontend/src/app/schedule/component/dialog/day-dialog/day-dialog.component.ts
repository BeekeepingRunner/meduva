import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {controlsConfig} from "../../../../component/clients/add-client/add-client.component";

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
  form!: FormGroup;

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
    });
  }

  onWorkHoursSave() {
    console.log(this.form.get('startTime')?.value);
    console.log(this.form.get('endTime')?.value);
    this.dialogRef.close();
  }
}
