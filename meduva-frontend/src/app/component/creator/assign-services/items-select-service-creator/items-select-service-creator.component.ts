import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Service} from "../../../../model/service";
import {ServicesService} from "../../../../service/services.service";
import {EquipmentListComponent} from "../../../equipment/equipment-list/equipment-list.component";
import {EquipmentModel} from "../../../../model/equipment";

@Component({
  selector: 'app-items-select-service-creator',
  templateUrl: './items-select-service-creator.component.html',
  styleUrls: ['./items-select-service-creator.component.css']
})
export class ItemsSelectServiceCreatorComponent extends EquipmentListComponent {

  selectedModels: EquipmentModel[] = [];

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  emitSelectedServicesIds() {

  }
}
