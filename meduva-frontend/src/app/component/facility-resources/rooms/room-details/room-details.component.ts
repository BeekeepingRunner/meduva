import {Component, Injectable, OnInit} from '@angular/core';
import {Room} from "../../../../model/room";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {RoomService} from "../../../../service/room.service";
import {ConfirmationDialogComponent} from "../../../dialog/confirmation-dialog/confirmation-dialog.component";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";
import {Service} from "../../../../model/service";
import {Visit} from "../../../../model/visit";
import {ConfirmationWithWarningDialogComponent} from "../../../dialog/confirmation-with-warning-dialog/confirmation-with-warning-dialog.component";

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
  visits: Visit[] = [];

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
    this.roomService.getRoomVisits(roomId).subscribe(
      visits => {
        this.visits=visits;
      })
  }

  deleteRoomFromList(roomId:number){
    this.roomService.getRoomVisits(roomId).subscribe(
      visits => {
        this.visits=visits;
        this.openConfirmationDialog(roomId);
      })
  }

  openConfirmationDialog(roomId?: number) {
      const confirmDialogRef = this.visits.length>0 ?
        this.dialog.open(ConfirmationWithWarningDialogComponent, {
          data: { message: 'There are booked visits associated with this room. If you want to abandon these visits, you must do it manually. Are you sure you want to delete them?' }
        })
        : this.dialog.open(ConfirmationDialogComponent, {
          data: { message: 'Do you want to delete this room?' }
        });

      confirmDialogRef.afterClosed().subscribe(confirmed => {
        if(confirmed) {
          if(roomId)
          this.deleteRoom(roomId);
          else{
            this.deleteRoom()
          }
        }
      });


  }

  private deleteRoom(roomId?:number) {
    if(!roomId){
      if(this.room.id) {
        roomId = this.room.id;
      }
      else{
        this.wasDeletionSuccessful = false;
        this.errorMessage = "You cannot delete room, because that room does not exist";
      }
    }
    if(this.wasDeletionSuccessful){

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
        if(this.router.url=="/rooms"){
          location.reload();
        }
        else{
          this.router.navigate(['/rooms']);
        }
      }
    );
  }


}
