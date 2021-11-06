import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {forbidValuesBetweenStep} from "../../../../util/validator/number-step";
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {NewServiceComponent} from "../../../services/new-service/new-service.component";
import {Room} from "../../../../model/room";
import {EquipmentItem, EquipmentModel} from "../../../../model/equipment";

export interface NewServiceRequest {
  service: Service,
  equipmentModels?: EquipmentModel[],
  roomItems: Room[]
}

@Component({
  selector: 'app-new-service-creator',
  templateUrl: './new-service-creator.component.html',
  styleUrls: ['./new-service-creator.component.css']
})
export class NewServiceCreatorComponent extends NewServiceComponent implements NewServiceRequest {
  @Input() roomItems: Room[] = [];
  @Input() eqModels: EquipmentModel[] = [];
  @Input() services: Service[] = [];

  @Input() selectedModels: EquipmentModel[] = [];
  serviceModels: EquipmentModel[] = [];

   service: Service = {
    name: '',
    description: '',
    durationInMin: 0,
    price: 0,
    itemless: false,
    deleted: false
  };




  onServiceCreated($event: Service){
    this.service=$event;
    console.log(this.services+"tgu")
  }

  onItemsLinked($event: EquipmentModel[]) {
    this.eqModels=$event;
  }

  onRoomsLinked($event: Room[]) {
    this.roomItems=$event;
  }


  onSelectedModelsGot($event: EquipmentModel[]) {
    this.selectedModels=$event;
  }

  onServiceReady() {
    let newServiceRequest: NewServiceRequest = {
      service: this.service,
      equipmentModels: this.eqModels,
      roomItems: this.roomItems
    }
    return newServiceRequest;

  }


  serviceModelsGot($event: EquipmentModel[]) {
    this.serviceModels = $event;
  }
}
