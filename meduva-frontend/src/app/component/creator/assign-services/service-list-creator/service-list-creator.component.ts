import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {ServiceListComponent} from "../../../facility-resources/services/service-list/service-list.component";
import {ConfigureEquipmentCreatorDialogComponent} from "../../../dialog/configure-equipment-creator-dialog/configure-equipment-creator-dialog.component";
import {EquipmentService} from "../../../../service/equipment.service";
import {MatDialog} from "@angular/material/dialog";
import {ConfigureServicesCreatorDialogComponent} from "../../../dialog/configure-services-creator-dialog/configure-services-creator-dialog.component";
import {Room} from "../../../../model/room";
import {EquipmentModel} from "../../../../model/equipment";

@Component({
  selector: 'app-service-list-creator',
  templateUrl: './service-list-creator.component.html',
  styleUrls: ['./service-list-creator.component.css']
})
export class ServiceListCreatorComponent extends ServiceListComponent {

  @Input() roomItems: Room[] = [];
  @Input() eqModels: EquipmentModel[] = [];

  @Output() roomConfigurationEmitter = new EventEmitter<Room[]>();
  @Output() equipmentConfigurationEmitter = new EventEmitter<EquipmentModel[]>();
  @Output() serviceConfigurationEmitter = new EventEmitter<Service[]>();

  servicesFromDB : Service[] = [];

  constructor(
    servicesService: ServicesService,
    private equipmentService: EquipmentService,
    public dialog: MatDialog,
  )
  {
    super(servicesService)
  }

  ngOnInit() {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.servicesFromDB = services;
      }
    )

    if(this.eqModels.length==0){
      this.equipmentService.getAllUndeletedEquipmentModels().subscribe(
        equipment => {
          this.eqModels=equipment;
        }
      );
    }
  }

  openServicesCreatorDialog() {
    const servicesCreatorDialogRef = this.dialog.open(ConfigureServicesCreatorDialogComponent,{
      data: {roomItems: this.roomItems, eqModels: this.eqModels, services: this.servicesFromDB}
    });
    servicesCreatorDialogRef.afterClosed().subscribe(serviceRequest => {
      if(serviceRequest){
        this.roomItems = serviceRequest.roomItems;
        this.eqModels = serviceRequest.equipmentModels
        this.services.push(serviceRequest.service);
        this.serviceConfigurationEmitter.emit(this.services);
      }
    });
  }

  emitConfiguration() {
    this.roomConfigurationEmitter.emit(this.roomItems);
    this.equipmentConfigurationEmitter.emit(this.eqModels);
    this.serviceConfigurationEmitter.emit(this.services);
  }
}
