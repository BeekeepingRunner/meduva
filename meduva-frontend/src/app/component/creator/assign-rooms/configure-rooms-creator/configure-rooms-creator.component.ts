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
  @Output() selectedIdsEmitter = new EventEmitter<Array<number>>();
  rooms: Room[] = [];
  selectedRoomIds!: Array<number>;

  constructor(
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.selectedRoomIds = new Array<number>();
  }


  openRoomSelectionDialog(item: Room) {
    const roomSelectionDialogRef = this.dialog.open(ConfigureRoomsCreatorDialogComponent, {
      data: { message: item.name }
    });
    roomSelectionDialogRef.afterClosed().subscribe(room => {
      let selectedRoom: Room = room[0];
      item = selectedRoom;
      // @ts-ignore
      this.selectedRoomIds[item.id - 1] = selectedRoom.id;
      this.selectedIdsEmitter.emit(this.selectedRoomIds);
    });
  }
}
