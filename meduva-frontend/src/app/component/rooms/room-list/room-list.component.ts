import { Component, OnInit } from '@angular/core';
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";

@Component({
  selector: 'app-room-list',
  templateUrl: './room-list.component.html',
  styleUrls: ['./room-list.component.css']
})
export class RoomListComponent implements OnInit {

  rooms: Room[] = [];
  displayedColumns: string[] = ['name'];

  constructor(protected roomService: RoomService) { }

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

}
