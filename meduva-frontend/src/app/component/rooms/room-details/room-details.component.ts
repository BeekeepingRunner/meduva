import { Component, OnInit } from '@angular/core';
import {Room} from "../../../model/room";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../service/room.service";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
export class RoomDetailsComponent implements OnInit {

  room!: Room;

  constructor(
    private route: ActivatedRoute,
    private roomService: RoomService,
    private router: Router,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    let roomId: number = Number(this.route.snapshot.params.id);
    this.roomService.getById(roomId).subscribe(
      room => {
        this.room = room;
      }
    );
  }

  openConfirmationDialog() {
    const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to delete this room?' }
    });

    confirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteRoom();
      }
    });
  }

  private deleteRoom() {
    this.roomService.deleteById(this.room.id).subscribe(
      ifSuccess => {
        this.router.navigate(['/rooms']);
      }
    );
  }
}
