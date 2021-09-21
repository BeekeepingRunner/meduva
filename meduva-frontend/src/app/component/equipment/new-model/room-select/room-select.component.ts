import {Component, Input, OnInit} from '@angular/core';
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

  @Input() eqItems!: EquipmentItem[];
  @Input() rooms!: Room[];
  displayedColumns: string[] = ['itemName', 'room', 'buttons'];

  selectedRoomIds: number[] = [];

  constructor(
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
  }

  openRoomSelectionDialog(item: EquipmentItem) {
    const roomSelectionDialogRef = this.dialog.open(RoomSelectionDialogComponent, {
      data: { message: item.name }
    });

    roomSelectionDialogRef.afterClosed().subscribe(room => {
      item.room = room[0];
    });
  }
}
