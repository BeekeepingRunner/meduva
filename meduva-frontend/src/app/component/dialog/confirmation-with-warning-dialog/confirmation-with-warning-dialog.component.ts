import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface ConfirmationWithWarningDialogData {
  message: string
}

@Component({
  selector: 'app-confirmation-with-warning-dialog',
  templateUrl: './confirmation-with-warning-dialog.component.html',
  styleUrls: ['./confirmation-with-warning-dialog.component.css']
})
export class ConfirmationWithWarningDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmationWithWarningDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationWithWarningDialogData
  ) { }
}
