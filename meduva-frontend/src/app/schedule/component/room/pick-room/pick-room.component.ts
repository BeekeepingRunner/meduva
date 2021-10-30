import { Component, OnInit } from '@angular/core';
import {User} from "../../../../model/user";
import {Room} from "../../../../model/room";
import {RoomService} from "../../../../service/room.service";

@Component({
  selector: 'app-pick-room',
  templateUrl: './pick-room.component.html',
  styleUrls: ['./pick-room.component.css']
})
export class PickRoomComponent implements OnInit {

  rooms: Room[] = [];
  displayedColumns: string[] = ['name'];

  constructor(
    private roomService: RoomService,
  ) { }

  ngOnInit(): void {
    this.roomService.getAllUndeletedRooms().subscribe(
      rooms => {
        this.rooms = rooms;
      }
    )
  }

}
