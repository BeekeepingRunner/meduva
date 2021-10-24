import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {NewRoomComponent} from "../../rooms/new-room/new-room.component";

export interface RoomSelectionDialogData {
  message: string
}

@Component({
  selector: 'app-configure-rooms-creator-dialog',
  templateUrl: './configure-rooms-creator-dialog.component.html',
  styleUrls: ['./configure-rooms-creator-dialog.component.css']
})
export class ConfigureRoomsCreatorDialogComponent extends NewRoomComponent{

  rooms: Room[] = [];
  selectedRoom!: Room;
  compareFunction = (o1: any, o2: any) => o1.id === o2.id;

  /*constructor(
    public dialogRef: MatDialogRef<ConfigureRoomsCreatorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RoomSelectionDialogData,
  ) { }*/


}
