import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Room} from "../../../../model/room";
import {MatDialog} from "@angular/material/dialog";
import {RoomSelectionDialogComponent} from "../../../dialog/room-selection-dialog/room-selection-dialog.component";
import {RoomService} from "../../../../service/room.service";
import {ConfigureRoomsCreatorDialogComponent} from "../../../dialog/configure-rooms-creator-dialog/configure-rooms-creator-dialog.component";

@Component({
  selector: 'app-configure-rooms-creator',
  templateUrl: './configure-rooms-creator.component.html',
  styleUrls: ['./configure-rooms-creator.component.css']
})
export class ConfigureRoomsCreatorComponent implements OnInit {

  displayedColumns: string[] = ['roomName', 'description', 'buttons'];

  @Input() roomItems!: Room[];
  @Output() roomItemsEmitter = new EventEmitter<Room[]>();
  rooms: Room[] = [];

  constructor(
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {

  }


  openRoomSelectionDialog(item: Room) {
    const roomSelectionDialogRef = this.dialog.open(ConfigureRoomsCreatorDialogComponent, {
      data: { name: item.name, description: item.description }
    });
    roomSelectionDialogRef.afterClosed().subscribe(room => {

      if(room!=undefined){
        let selectedRoom: Room = room;
        item.name=selectedRoom.name;
        item.description=selectedRoom.description;
        this.roomItems.splice(this.roomItems.indexOf(item),1,selectedRoom)
        // @ts-ignore
        console.log(this.roomItems)
        this.roomItemsEmitter.emit(this.roomItems);
      }

    });
  }
}
