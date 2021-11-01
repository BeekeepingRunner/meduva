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
import {EquipmentModel} from "../../../model/equipment";

export interface roomData {
  roomItems: Room[];
  eqModels: EquipmentModel[];
}
@Component({
  selector: 'app-configure-services-creator-dialog',
  templateUrl: './configure-services-creator-dialog.component.html',
  styleUrls: ['./configure-services-creator-dialog.component.css']
})
export class ConfigureServicesCreatorDialogComponent {
  @Output() roomItems: Room[] = [];
  @Output() eqModels: EquipmentModel[] = [];
  @ViewChild(NewModelCreatorComponent)
  private newModelCreatorComponent!: NewModelCreatorComponent;

  constructor(
    public dialogRef: MatDialogRef<ConfigureServicesCreatorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: roomData
  ) {
    this.roomItems=data.roomItems;
    this.eqModels=data.eqModels;
  }

}
