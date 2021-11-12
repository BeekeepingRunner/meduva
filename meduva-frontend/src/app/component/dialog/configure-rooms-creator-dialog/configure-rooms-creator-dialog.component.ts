import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {NewRoomComponent} from "../../facility-resources/rooms/new-room/new-room.component";
import {FormBuilder} from "@angular/forms";

export interface ConfigureRoomsCreatorDialogData {
  name: string
  description: string
  roomItems: Room[]
}

@Component({
  selector: 'app-configure-rooms-creator-dialog',
  templateUrl: './configure-rooms-creator-dialog.component.html',
  styleUrls: ['./configure-rooms-creator-dialog.component.css']
})
export class ConfigureRoomsCreatorDialogComponent extends NewRoomComponent{

  defaultRoomName: string = '';
  rooms: Room[] = [];
  selectedRoom: Room = {
    name: "",
    description: "",
    deleted: false,
    services: []
  };

  constructor(
     formBuilder: FormBuilder,
     roomService: RoomService,
    @Inject(MAT_DIALOG_DATA) public data: ConfigureRoomsCreatorDialogData,
  ) {
    super(formBuilder,roomService);
    this.rooms = data.roomItems;
    this.selectedRoom.name = data.name;
    this.defaultRoomName = data.name;
    this.selectedRoom.description = data.description;
  }


  checkIfRoomNameIsTaken() {
    for(let anyRoom of this.rooms){
      if(anyRoom.name == this.defaultRoomName){
        continue;
      }
      if(this.selectedRoom.name==anyRoom.name)
        return true;
    }
    return false;
  }
}
