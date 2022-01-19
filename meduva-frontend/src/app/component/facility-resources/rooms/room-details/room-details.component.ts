import {Component, Injectable, OnInit} from '@angular/core';
import {Room} from "../../../../model/room";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../../service/room.service";
import {ConfirmationDialogComponent} from "../../../dialog/confirmation-dialog/confirmation-dialog.component";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
@Injectable({
  providedIn: 'root'
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
    this.getRoomInformation();
  }

  getRoomInformation(){
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

  openConfirmationDialog(roomId: number) {
    const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to delete this room?' }
    });

    confirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteRoom(roomId);
      }
      else{
        if(roomId!=-1){
          this.router.navigateByUrl("/rooms");
        }
      }
    });
  }

  private deleteRoom(roomId:number) {
    if(roomId==-1){
      if(this.room.id) {
        roomId = this.room.id;
      }
      else{
        this.wasDeletionSuccessful = false;
        this.errorMessage = "You cannot delete room, because that room does not exist";
      }
    }
    if(this.wasDeletionSuccessful==true){
      this.roomService.deleteById(roomId).subscribe(
        ifSuccess => {
          this.openFeedbackDialog();
        },
        err => {
          this.wasDeletionSuccessful = false;
          this.errorMessage = err.error.message;
        }
      );
    }

  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Room has been deleted.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/rooms']);
      }
    );
  }
}
