import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

// decimalPrecision must be a non-negative integer
export function forbidValuesBetweenStep(stepValue: number, decimalPlaces: number = 0): ValidatorFn {

  return (control: AbstractControl): ValidationErrors | null => {

    let controlValue: number = control.value;

    // we convert parameters to integers
    let multiplier: number = Math.pow(10, decimalPlaces);
    controlValue = Math.round(controlValue * multiplier);
    stepValue = Math.round(stepValue * multiplier);

    let isValueCorrect: boolean = controlValue % stepValue == 0;

    // we need to set this variable to previous value because somehow it isn't
    // refreshed every time that function is executed
    stepValue /= multiplier;

    return isValueCorrect
      ? null : {wrongValue : true};
  }
}
