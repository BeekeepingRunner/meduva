import { Component, OnInit } from '@angular/core';
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";
import {ServiceListComponent} from "../../../services/service-list/service-list.component";
import {ConfigureEquipmentCreatorDialogComponent} from "../../../dialog/configure-equipment-creator-dialog/configure-equipment-creator-dialog.component";
import {EquipmentService} from "../../../../service/equipment.service";
import {MatDialog} from "@angular/material/dialog";
import {ConfigureServicesCreatorDialogComponent} from "../../../dialog/configure-services-creator-dialog/configure-services-creator-dialog.component";

@Component({
  selector: 'app-service-list-creator',
  templateUrl: './service-list-creator.component.html',
  styleUrls: ['./service-list-creator.component.css']
})
export class ServiceListCreatorComponent extends ServiceListComponent {

  constructor(
    servicesService: ServicesService,
    public dialog: MatDialog,
  ) {
  super(servicesService)

  }

  openServicesCreatorDialog() {
    const equipmentCreatorDialogRef = this.dialog.open(ConfigureServicesCreatorDialogComponent,{
      data: {}
    });
  }
}
