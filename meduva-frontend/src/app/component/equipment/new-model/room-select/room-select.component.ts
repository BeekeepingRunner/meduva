import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Room} from "../../../../model/room";
import {EquipmentItem} from "../../../../model/equipment";
import {MatDialog} from "@angular/material/dialog";
import {RoomSelectionDialogComponent} from "../../../dialog/room-selection-dialog/room-selection-dialog.component";
import {RoomService} from "../../../../service/room.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-room-select',
  templateUrl: './room-select.component.html',
  styleUrls: ['./room-select.component.css']
})
export class RoomSelectComponent implements OnInit {

  displayedColumns: string[] = ['itemName', 'room', 'buttons'];

  @Input() eqItems!: EquipmentItem[];
  @Output() selectedIdsEmitter = new EventEmitter<Array<number>>();
  @Output() itemsEmitter = new EventEmitter<EquipmentItem[]>();
  eqItemsOutput: EquipmentItem[] = [];

  @Input() rooms: Room[] = [];

  @Input() creatorRooms: Room[] = [];
  /**Because of the fact that this class is used in two cases, during adding the room and in the creator, there is an input annotation.
   Creator uses that class in the string of giving data among the dialogs and classes*/

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
       this.rooms=rooms;
       for(let creatorRoom of this.creatorRooms){
         this.rooms.unshift(creatorRoom);
       }
      }
    );
  }


  openRoomSelectionDialog(item: EquipmentItem) {
    const roomSelectionDialogRef = this.dialog.open(RoomSelectionDialogComponent, {
      data: { message: item.name, roomItems: this.rooms }
    });

    roomSelectionDialogRef.afterClosed().subscribe(room => {
      let selectedRoom: Room = room[0];
      item.room = selectedRoom;
      this.eqItemsOutput.push(item);

      // @ts-ignore
      this.selectedRoomIds[item.id - 1] = selectedRoom.id;
      this.selectedIdsEmitter.emit(this.selectedRoomIds);

      if(this.eqItemsOutput.length==this.eqItems.length)
      this.itemsEmitter.emit(this.eqItemsOutput)

    });
  }
}
