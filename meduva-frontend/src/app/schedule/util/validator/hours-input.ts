import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";
import {WorkHours} from "../../service/schedule.service";

export const startTimeBeforeEndTimeValidator
  : ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const startTime: string = control.get('startTime')?.value;
  const endTime: string = control.get('endTime')?.value;

  let isInCorrectOrder: boolean
    = (startTime < endTime) && (startTime != endTime);
  return !isInCorrectOrder ? { inWrongOrder: true } : null;
};

export function absencesValidator(existingWorkHours: WorkHours): ValidatorFn {

    return (control: AbstractControl): { [key: string]: boolean } | null => {
        if( existingWorkHours === undefined){
          return { 'workHoursUndefined': true};
        } else{
          const startTime: string = control.get('startTime')?.value + ":00";
          const endTime: string = control.get('endTime')?.value + ":00";

          existingWorkHours.startTime = new Date(existingWorkHours.startTime);
          existingWorkHours.endTime = new Date(existingWorkHours.endTime);

          let dayWorkStart: string = existingWorkHours.startTime.toLocaleTimeString();
          let dayWorkEnd: string = existingWorkHours.endTime.toLocaleTimeString();

          if(!((startTime >= dayWorkStart) && (endTime <= dayWorkEnd))){
            return { 'absenceHoursWithinWorkHours': true}
          }

          return null;
        }
            }
};


