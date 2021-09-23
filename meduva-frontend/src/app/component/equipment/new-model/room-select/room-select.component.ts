import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Room} from "../../../../model/room";
import {EquipmentItem} from "../../../../model/equipment";
import {MatDialog} from "@angular/material/dialog";
import {RoomSelectionDialogComponent} from "../../../dialog/room-selection-dialog/room-selection-dialog.component";
import {RoomService} from "../../../../service/room.service";

@Component({
  selector: 'app-room-select',
  templateUrl: './room-select.component.html',
  styleUrls: ['./room-select.component.css']
})
export class RoomSelectComponent implements OnInit {

  displayedColumns: string[] = ['itemName', 'room', 'buttons'];

  rooms: Room[] = [];
  @Input() eqItems!: EquipmentItem[];
  @Output() selectedIdsEmitter = new EventEmitter<Array<number>>();
  selectedRoomIds!: Array<number>;

  constructor(
    public dialog: MatDialog,
    private roomService: RoomService
  ) { }

  ngOnInit(): void {
    this.selectedRoomIds = new Array<number>();
    this.fetchRooms();
  }

  fetchRooms() {
    this.roomService.getAllUndeletedRooms().subscribe(
      rooms => {
        this.rooms = rooms;
      }
    );
  }

  openRoomSelectionDialog(item: EquipmentItem) {
    const roomSelectionDialogRef = this.dialog.open(RoomSelectionDialogComponent, {
      data: { message: item.name }
    });

    roomSelectionDialogRef.afterClosed().subscribe(room => {
      let selectedRoom: Room = room[0];
      item.room = selectedRoom;
      // @ts-ignore
      this.selectedRoomIds[item.id - 1] = selectedRoom.id;
      this.selectedIdsEmitter.emit(this.selectedRoomIds);
    });
  }
}
