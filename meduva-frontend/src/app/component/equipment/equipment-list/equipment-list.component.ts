import { Component, OnInit } from '@angular/core';
import {EquipmentModel} from "../../../model/equipment-model";
import {EquipmentService} from "../../../service/equipment.service";

@Component({
  selector: 'app-equipment-list',
  templateUrl: './equipment-list.component.html',
  styleUrls: ['./equipment-list.component.css']
})
export class EquipmentListComponent implements OnInit {

  models: EquipmentModel[] = [];
  displayedColumns: string[] = ['name'];

  constructor(
    private equipmentService: EquipmentService,
  ) { }

  ngOnInit(): void {
    this.equipmentService.getAllUndeletedEquipmentModels().subscribe(
      models => {
        this.models = models
        console.log(models);
      },
      err => {
        console.log(err);
      }
    )
  }

}
