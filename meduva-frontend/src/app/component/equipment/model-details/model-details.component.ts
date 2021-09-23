import { Component, OnInit } from '@angular/core';
import {EquipmentItem, EquipmentModel} from "../../../model/equipment";
import {ActivatedRoute} from "@angular/router";
import {EquipmentService} from "../../../service/equipment.service";
import {Service} from "../../../model/service";

@Component({
  selector: 'app-model-details',
  templateUrl: './model-details.component.html',
  styleUrls: ['./model-details.component.css']
})
export class ModelDetailsComponent implements OnInit {

  model!: EquipmentModel;
  modelServices: Service[] = [];
  modelItems: EquipmentItem[] = [];

  serviceTableColumns: string[] = ['title'];
  itemTableColumns: string[] = ['name', 'room'];

  constructor(
    private activatedRoute: ActivatedRoute,
    private equipmentService: EquipmentService,
  ) { }

  ngOnInit(): void {
    let modelId = this.activatedRoute.snapshot.params.id;
    this.equipmentService.getModelById(modelId).subscribe(
      model => {
        this.model = model;
        this.modelServices = model.services;
        this.modelItems = model.items;
      },
      err => {
        console.log(err);
      }
    );
  }

  openConfirmationDialog() {

  }
}
