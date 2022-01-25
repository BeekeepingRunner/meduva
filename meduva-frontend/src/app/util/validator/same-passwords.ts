import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export const samePasswordsValidator: ValidatorFn =
  (control: AbstractControl): ValidationErrors | null => {

  const newPass = control.get('newPass');
  const repeatNewPass = control.get('repeatNewPass');

  return newPass && repeatNewPass && newPass.value !== repeatNewPass.value ?
    { differentPasswords: "passwords must be the same" } : null;
}


