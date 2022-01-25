import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Service} from "../../../../model/service";
import {ServicesService} from "../../../../service/services.service";
import {EquipmentListComponent} from "../../../facility-resources/equipment/equipment-list/equipment-list.component";
import {EquipmentModel} from "../../../../model/equipment";

@Component({
  selector: 'app-items-select-service-creator',
  templateUrl: './items-select-service-creator.component.html',
  styleUrls: ['./items-select-service-creator.component.css']
})
export class ItemsSelectServiceCreatorComponent extends EquipmentListComponent {

  @Input() service!: Service;

  @Output() relatedItemsEmitter = new EventEmitter<EquipmentModel[]>();
  @Output() serviceEmitter = new EventEmitter<Service>();
  @Output() selectedItemsEmitter = new EventEmitter<EquipmentModel[]>();
  @Output() serviceModelsEmitter = new EventEmitter<EquipmentModel[]>();

  @Input() creatorModels: EquipmentModel[] = [];
  @Input() doNotMultiplyServiceInModels: EquipmentModel[] = [];
  @Input() selectedModels: EquipmentModel[] = [];

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;


  ngOnInit() {
    for(let model of this.creatorModels){
      this.models.push(model);
    }
  }

  assignEquipmentToService() {

    for(let clearRecentlyAddedServiceFromModel of this.models){
      for(let checkInModelsOfServiceArray of this.doNotMultiplyServiceInModels){
        if(checkInModelsOfServiceArray.name==clearRecentlyAddedServiceFromModel.name){
          clearRecentlyAddedServiceFromModel.services.pop();
        }
      }
    }

    this.doNotMultiplyServiceInModels=[];

    for (let selectedModel of this.selectedModels){
      for(let properModel of this.models){
        if(selectedModel==properModel){
            this.doNotMultiplyServiceInModels.push(properModel);
            properModel.services.push(this.service);
        }
      }
    }

    this.emitServiceConfiguration();
  }

  clearSelection() {
    this.selectedModels.length=0;
    this.selectedModels = [];
  }

  emitServiceConfiguration(){
    this.relatedItemsEmitter.emit(this.models);
    this.serviceEmitter.emit(this.service);
    this.selectedItemsEmitter.emit(this.selectedModels);
    this.serviceModelsEmitter.emit(this.doNotMultiplyServiceInModels);
  }

}
