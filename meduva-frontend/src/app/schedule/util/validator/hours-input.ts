import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export const startTimeBeforeEndTimeValidator
  : ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const startTime: string = control.get('startTime')?.value;
  const endTime: string = control.get('endTime')?.value;

  let isInCorrectOrder: boolean
    = (startTime < endTime) && (startTime != endTime);
  return !isInCorrectOrder ? { inWrongOrder: true } : null;
};
