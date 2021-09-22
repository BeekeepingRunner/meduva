import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Room} from "../../../../model/room";
import {EquipmentItem} from "../../../../model/equipment";
import {MatDialog} from "@angular/material/dialog";
import {RoomSelectionDialogComponent} from "../../../dialog/room-selection-dialog/room-selection-dialog.component";

@Component({
  selector: 'app-room-select',
  templateUrl: './room-select.component.html',
  styleUrls: ['./room-select.component.css']
})
export class RoomSelectComponent implements OnInit {

  displayedColumns: string[] = ['itemName', 'room', 'buttons'];

  @Input() eqItems!: EquipmentItem[];
  @Input() rooms!: Room[];
  @Output() selectedIdsEmmitter = new EventEmitter<Array<number>>();
  selectedRoomIds!: Array<number>;

  constructor(
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.selectedRoomIds = new Array<number>(this.rooms.length);
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
      this.selectedIdsEmmitter.emit(this.selectedRoomIds);
      console.log(this.selectedRoomIds);
    });
  }
}
