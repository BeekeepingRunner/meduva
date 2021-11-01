import {Component, Input, OnInit} from '@angular/core';
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {ServiceListComponent} from "../../../services/service-list/service-list.component";
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

  constructor(
    servicesService: ServicesService,
    public dialog: MatDialog,
  ) {
  super(servicesService)

  }

  openServicesCreatorDialog() {
    const servicesCreatorDialogRef = this.dialog.open(ConfigureServicesCreatorDialogComponent,{
      data: {roomItems: this.roomItems, eqModels: this.eqModels}
    });
  }
}
