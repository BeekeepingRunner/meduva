import { Component, OnInit } from '@angular/core';
import {EquipmentModel} from "../../../model/equipment";
import {ActivatedRoute} from "@angular/router";
import {EquipmentService} from "../../../service/equipment.service";

@Component({
  selector: 'app-model-details',
  templateUrl: './model-details.component.html',
  styleUrls: ['./model-details.component.css']
})
export class ModelDetailsComponent implements OnInit {

  model!: EquipmentModel;

  constructor(
    private activatedRoute: ActivatedRoute,
    private equipmentService: EquipmentService,
  ) { }

  ngOnInit(): void {
    let modelId = this.activatedRoute.snapshot.params.id;
    this.equipmentService.getModelById(modelId).subscribe(
      model => {
        this.model = model;
      },
      err => {
        console.log(err);
      }
    );
  }

  openConfirmationDialog() {

  }
}
