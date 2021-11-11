import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {Client} from "../../../model/client";
import {UserService} from "../../../service/user.service";
import {ServicesService} from "../../../service/services.service";
import {RoomService} from "../../../service/room.service";
import {EquipmentService} from "../../../service/equipment.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {ClientService} from "../../../service/client.service";

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {

  term!: Term | null;
  clientInfo!: string;
  workerInfo!: string;
  serviceName!: string;
  roomName!: string;
  eqItemName!: string;

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private clientService: ClientService,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.term = this.visitService.getSelectedTerm();
    if (this.term != null) {
      this.setClientInfo();
      this.setWorkerInfo();
      this.setServiceName();
      this.setRoomName();
      this.setEqItemName();
    }
  }

  private setClientInfo() {
    if (this.term?.clientUnregistered) {
      this.clientService.getClientById(this.term.clientId).subscribe(
        client => {
          this.clientInfo = client.name + ' ' + client.surname + ' (' + client.phoneNumber + ')';
        },
        err => {
          console.log(err);
        }
      );
    } else {
      this.userService.getUserDetails(this.term!.clientId).subscribe(
        client => {
          this.clientInfo = client.name + ' ' + client.surname + ' (' + client.phoneNumber + ')'
            + ' (' + client.email + ')';
        },
        err => {
          console.log(err);
        }
      );
    }
  }

  private setWorkerInfo() {
    this.userService.getUserDetails(this.term!.workerId).subscribe(
      worker => {
        this.workerInfo = worker.name + ' ' + worker.surname + ' (' + worker.phoneNumber + ')';
      }
    );
  }

  private setServiceName() {
    this.servicesService.getById(this.term!.serviceId).subscribe(
      service => {
        this.serviceName = service.name;
      }
    );
  }

  private setRoomName() {
    this.roomService.getById(this.term!.roomId).subscribe(
      room => {
        this.roomName = room.name;
      }
    );
  }

  private setEqItemName() {
    this.equipmentService.getItemById(this.term!.eqItemId).subscribe(
      eqItem => {
        this.eqItemName = eqItem.name;
      }
    );
  }

  submitVisit(): void {
    this.visitService.saveVisit(this.term).subscribe(
      visitData => {
        this.openFeedbackDialog();
      }
    )
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: {message: 'Visit has been saved.'}
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/visit/pick-service']);
      }
    );
  }
}
