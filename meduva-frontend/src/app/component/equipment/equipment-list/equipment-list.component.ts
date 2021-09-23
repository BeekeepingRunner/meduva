import { Component, OnInit } from '@angular/core';
import {EquipmentModel} from "../../../model/equipment";
import {EquipmentService} from "../../../service/equipment.service";

@Component({
  selector: 'app-equipment-list',
  templateUrl: './equipment-list.component.html',
  styleUrls: ['./equipment-list.component.css']
})
export class EquipmentListComponent implements OnInit {

  models: EquipmentModel[] = [];
  modelTableColumns: string[] = ['name'];
  itemTableColumns: string[] = ['name', 'room'];

  constructor(
    private equipmentService: EquipmentService,
  ) { }

  ngOnInit(): void {
    this.getModels();
  }

  private getModels() {
    this.equipmentService.getAllUndeletedEquipmentModels().subscribe(
      models => {
        this.models = this.excludeDeletedItems(models);
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
}
