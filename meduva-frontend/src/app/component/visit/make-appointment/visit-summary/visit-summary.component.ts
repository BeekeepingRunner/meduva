import {Component, Input, OnInit} from '@angular/core';
import {Term, VisitService} from "../../../../service/visit.service";
import {UserService} from "../../../../service/user.service";
import {ClientService} from "../../../../service/client.service";
import {ServicesService} from "../../../../service/services.service";
import {RoomService} from "../../../../service/room.service";
import {EquipmentService} from "../../../../service/equipment.service";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";
import {Client} from "../../../../model/client";
import {User} from "../../../../model/user";
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-visit-summary',
  templateUrl: './visit-summary.component.html',
  styleUrls: ['./visit-summary.component.css']
})
export class VisitSummaryComponent implements OnInit {

  @Input() client!: Client;
  @Input() worker!: User;
  @Input() service!: Service;
  @Input() term!: Term;

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
    this.setRoomName();
    this.setEqItemName();
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
}
