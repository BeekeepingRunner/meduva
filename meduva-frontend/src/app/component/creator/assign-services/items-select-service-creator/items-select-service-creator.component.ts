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
  @Input() creatorModels: EquipmentModel[] = [];
  @Input() service!: Service;

  @Output() relatedItemsEmitter = new EventEmitter<EquipmentModel[]>();
  @Output() serviceEmitter = new EventEmitter<Service>();
  @Output() selectedItemsEmitter = new EventEmitter<EquipmentModel[]>();

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  emitSelectedServicesIds() {
  }
  ngOnInit() {
    for(let model of this.creatorModels){
      this.models.push(model);
    }
  }

  assignEquipmentToService() {
    for (let selectedModel of this.selectedModels){
      for(let properModel of this.models){
        if(selectedModel==properModel){
          properModel.services.push(this.service);
        }
      }
    }
    this.relatedItemsEmitter.emit(this.models);
    this.serviceEmitter.emit(this.service);
    this.selectedItemsEmitter.emit(this.selectedModels);
  }

  clearSelection() {
    this.selectedModels=[];
  }


}
