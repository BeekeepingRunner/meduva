import { Component, OnInit } from '@angular/core';
import {EquipmentModel} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";
import {ConfigureRoomsCreatorDialogComponent} from "../../../dialog/configure-rooms-creator-dialog/configure-rooms-creator-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ConfigureEquipmentCreatorDialogComponent} from "../../../dialog/configure-equipment-creator-dialog/configure-equipment-creator-dialog.component";

@Component({
  selector: 'app-equipment-list-creator',
  templateUrl: './equipment-list-creator.component.html',
  styleUrls: ['./equipment-list-creator.component.css']
})
export class EquipmentListCreatorComponent implements OnInit {

  models: EquipmentModel[] = [];
  modelTableColumns: string[] = ['name'];
  itemTableColumns: string[] = ['name', 'room', 'status'];

  constructor(
    private equipmentService: EquipmentService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.getModels();
  }

  private getModels() {
    this.equipmentService.getAllUndeletedEquipmentModels().subscribe(
      models => {
        this.models = this.excludeDeletedItems(models);
        console.log(this.models);
      },
      err => {
        console.log(err);
      }
    );
  }

  private excludeDeletedItems(models: EquipmentModel[]): EquipmentModel[] {
    models.forEach(model => {
      model.items = model.items.filter(item => !item.deleted);
    });
    return models;
  }

  openConfigureEquipmentCreatorDialog() {
    const equipmentCreatorDialogRef = this.dialog.open(ConfigureEquipmentCreatorDialogComponent);
  }
}
