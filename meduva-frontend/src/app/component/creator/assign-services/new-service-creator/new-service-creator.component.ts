import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {forbidValuesBetweenStep} from "../../../../util/validator/number-step";
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {NewServiceComponent} from "../../../services/new-service/new-service.component";
import {Room} from "../../../../model/room";
import {EquipmentItem, EquipmentModel} from "../../../../model/equipment";

@Component({
  selector: 'app-new-service-creator',
  templateUrl: './new-service-creator.component.html',
  styleUrls: ['./new-service-creator.component.css']
})
export class NewServiceCreatorComponent extends NewServiceComponent {
  @Input() roomItems: Room[] = [];
  @Input() eqModels: EquipmentModel[] = [];

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
  }

  onItemsLinked($event: EquipmentModel[]) {
    this.eqModels=$event;
  }
}
