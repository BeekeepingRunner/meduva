import { Component, OnInit } from '@angular/core';
import {Room} from "../../../model/room";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../service/room.service";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {Service} from "../../../model/service";

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
export class RoomDetailsComponent implements OnInit {

  room!: Room;
  wasDeletionSuccessful: boolean = true;
  errorMessage: string = '';
  columnName: string[] = ['Name'];
  services: Service[] = [];

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
      this.roomService.getRoomServices(this.room.id!).subscribe(
        services => {
          this.services = services;
        }
      )
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
        this.openFeedbackDialog();
      },
      err => {
        this.wasDeletionSuccessful = false;
        this.errorMessage = err.error.message;
      }
    );
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Room ' + this.room.name + ' has been deleted.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/rooms']);
      }
    );
  }
}
