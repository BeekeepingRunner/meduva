import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Service} from "../../../../model/service";
import {ServicesService} from "../../../../service/services.service";
import {EquipmentModel} from "../../../../model/equipment";
import {RoomListComponent} from "../../../rooms/room-list/room-list.component";
import {Room} from "../../../../model/room";

@Component({
  selector: 'app-rooms-select-service-creator',
  templateUrl: './rooms-select-service-creator.component.html',
  styleUrls: ['./rooms-select-service-creator.component.css']
})
export class RoomsSelectServiceCreatorComponent extends RoomListComponent {

  selectedRooms: Room[] = [];
  automatically : boolean = true;

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  emitSelectedServicesIds() {

  }

  changeAutoState(){
    if(this.automatically)
    this.automatically=false;
    else
      this.automatically=true;
}
}
