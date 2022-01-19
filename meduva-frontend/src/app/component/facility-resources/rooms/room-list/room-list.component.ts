import {Component, OnInit, ViewChild} from '@angular/core';
import {Room} from "../../../../model/room";
import {RoomService} from "../../../../service/room.service";
import {RoomDetailsComponent} from "../room-details/room-details.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-room-list',
  templateUrl: './room-list.component.html',
  styleUrls: ['./room-list.component.css']
})
export class RoomListComponent implements OnInit {

  rooms: Room[] = [];
  displayedColumns: string[] = ['name','delete'];

  constructor(
    protected roomService: RoomService,
    private roomDetailsComponent: RoomDetailsComponent,
    private router:Router) { }

  ngOnInit(): void {
    this.getAllRooms()
  }

  getAllRooms() {
    this.roomService.getAllUndeletedRooms().subscribe(
      rooms => {
        this.rooms = rooms;
      }
    )
  }

  deleteRoomFromList(roomToDeleteId:number) {
   this.roomDetailsComponent.openConfirmationDialog(roomToDeleteId);

  }
}
