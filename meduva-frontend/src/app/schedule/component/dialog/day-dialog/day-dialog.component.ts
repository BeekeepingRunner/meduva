import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {
  absenceValidator,
  startTimeBeforeEndTimeValidator
} from "../../../util/validator/hours-input";
import {ScheduleService, WeekBoundaries, WorkHours} from "../../../service/schedule.service";


export interface DayDialogData {
  date: Date;
  workerId: number;
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
  deletingWorkOrAbsenceHours: boolean = false;
  form!: FormGroup;

  workHours!: WorkHours;
  existingWorkingHours!: WorkHours | undefined;

  noAbsenceHoursError: boolean = false;


  constructor(
    public dialogRef: MatDialogRef<DayDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DayDialogData,
    private formBuilder: FormBuilder,
    private scheduleService: ScheduleService,
  ) {
  }

  ngOnInit(): void {
    this.selectedDate = this.data.date;
    this.dateString = this.data.date.toDateString();
    this.data.date
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
    let dayBoundaries: WeekBoundaries = {
      firstWeekDay: this.selectedDate,
      lastWeekDay: this.selectedDate
    }
    this.scheduleService.getWeeklyWorkHours(this.data.workerId, dayBoundaries).subscribe(
      data => {
        this.settingExistingWorkingHoursVariable(data);
        this.prepareAbsenceHoursForm();
      }
    );
  }

  private prepareAbsenceHoursForm() {
    this.settingAbsenceHours = true;
    this.form = this.formBuilder.group({
      startTime: new FormControl('', [Validators.required]),
      endTime: new FormControl('', [Validators.required]),
    },);
    this.form.setValidators([absenceValidator(this.existingWorkingHours!),
      startTimeBeforeEndTimeValidator]);
  }

  private settingExistingWorkingHoursVariable(data: WorkHours[]) {
    let theExistingWorkingHoursTable = data;
    this.existingWorkingHours = theExistingWorkingHoursTable[0];
  }

  onAbsenceHoursSave(){
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
      event: 'ABSENCE_HOURS',
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

  setWholeDay(){
    this.form.get('startTime')?.patchValue(this.existingWorkingHours!.startTime.toLocaleTimeString().slice(0,5));
    this.form.get('endTime')?.patchValue(this.existingWorkingHours!.endTime.toLocaleTimeString().slice(0,5));
    this.form.markAllAsTouched();
  }

  chooseWorkHoursOrAbsence(){
    if(this.settingWorkHours){
      this.onWorkHoursSave()
    } else {
      this.onAbsenceHoursSave();
    }
  }

  startDeletingHours(){
    this.deletingWorkOrAbsenceHours = true;
  }

  tryToDeleteDailyAbsenceHours(){
      //sprawdzic czy absence hours są: jesli tak zakmniac okno, jesli nie wyswietlic komunikat ze takowych nie ma
    let dayBoundaries: WeekBoundaries = {
      firstWeekDay: this.selectedDate,
      lastWeekDay: this.selectedDate
    }

    this.scheduleService.getWeeklyAbsenceHours(this.data.workerId, dayBoundaries).subscribe(
      data => {
        if(data.length != 0){
          console.log("przeszlo");
          this.dialogRef.close({
            event: 'DELETE_ABSENCE_HOURS',
            data: this.selectedDate
          });
        } else{
          this.noAbsenceHoursError = true;
          console.log("blad?");
        }
      }
    );
  }


}
