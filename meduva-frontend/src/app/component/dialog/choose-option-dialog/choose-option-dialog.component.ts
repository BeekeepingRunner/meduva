import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface ConfirmationDialogData {
  message: string,
  optionNo: string,
  optionYes: string
}

@Component({
  selector: 'app-choose-option-dialog',
  templateUrl: './choose-option-dialog.component.html',
  styleUrls: ['./choose-option-dialog.component.css']
})
export class ChooseOptionDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ChooseOptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogData
  ) { }
}
