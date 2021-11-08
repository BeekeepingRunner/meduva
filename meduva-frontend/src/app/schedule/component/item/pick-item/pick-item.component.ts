import { Component, OnInit } from '@angular/core';
import {EquipmentModel} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";

@Component({
  selector: 'app-pick-item',
  templateUrl: './pick-item.component.html',
  styleUrls: ['./pick-item.component.css']
})
export class PickItemComponent implements OnInit {

  models: EquipmentModel[] = [];
  modelTableColumns: string[] = ['name'];
  itemTableColumns: string[] = ['name', 'room', 'status'];

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
}
