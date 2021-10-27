import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {NewRoomComponent} from "../../rooms/new-room/new-room.component";
import {FormBuilder} from "@angular/forms";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from "../confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-configure-equipment-creator-dialog',
  templateUrl: './configure-equipment-creator-dialog.component.html',
  styleUrls: ['./configure-equipment-creator-dialog.component.css']
})
export class ConfigureEquipmentCreatorDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfigureEquipmentCreatorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogData
  ) { }
}
