import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {NewRoomComponent} from "../../rooms/new-room/new-room.component";
import {FormBuilder} from "@angular/forms";

export interface ConfigureRoomsCreatorDialogData {
  name: string
  description: string
}

@Component({
  selector: 'app-configure-rooms-creator-dialog',
  templateUrl: './configure-rooms-creator-dialog.component.html',
  styleUrls: ['./configure-rooms-creator-dialog.component.css']
})
export class ConfigureRoomsCreatorDialogComponent extends NewRoomComponent{

  rooms: Room[] = [];
  selectedRoom: Room = {
    name: "",
    description: "",
    deleted: false
  };

  constructor(
     formBuilder: FormBuilder,
     roomService: RoomService,
    @Inject(MAT_DIALOG_DATA) public data: ConfigureRoomsCreatorDialogData,
  ) {
    super(formBuilder,roomService);
  }



}
