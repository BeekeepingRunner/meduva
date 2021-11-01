import {Component, Inject, OnInit, Output, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {NewRoomComponent} from "../../rooms/new-room/new-room.component";
import {FormBuilder} from "@angular/forms";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from "../confirmation-dialog/confirmation-dialog.component";
import {NewModelCreatorComponent} from "../../creator/assign-equipment/new-model-creator/new-model-creator.component";
import {EquipmentListCreatorComponent} from "../../creator/assign-equipment/equipment-list-creator/equipment-list-creator.component";
import {EquipmentItem} from "../../../model/equipment";

export interface roomData {
  roomItems: Room[];
}
@Component({
  selector: 'app-configure-equipment-creator-dialog',
  templateUrl: './configure-equipment-creator-dialog.component.html',
  styleUrls: ['./configure-equipment-creator-dialog.component.css']
})
export class ConfigureEquipmentCreatorDialogComponent {
  @Output() roomItems: Room[] = [];
  @ViewChild(NewModelCreatorComponent)
  private newModelCreatorComponent!: NewModelCreatorComponent;

  constructor(
    public dialogRef: MatDialogRef<ConfigureEquipmentCreatorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: roomData
  ) {
    this.roomItems=data.roomItems;
  }


}
