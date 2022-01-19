import { Component, OnInit } from '@angular/core';
import {EquipmentItem, EquipmentModel} from "../../../../model/equipment";
import {ActivatedRoute, Router} from "@angular/router";
import {EquipmentService} from "../../../../service/equipment.service";
import {Service} from "../../../../model/service";
import {ConfirmationDialogComponent} from "../../../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {FeedbackDialogComponent} from "../../../dialog/feedback-dialog/feedback-dialog.component";

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
  itemTableColumns: string[] = ['name', 'room', 'status'];

  constructor(
    private activatedRoute: ActivatedRoute,
    private equipmentService: EquipmentService,
    private dialog: MatDialog,
    private router: Router,
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

  openDeleteConfirmDialog(): void {
    const deleteConfirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to delete this model with its all items?' }
    });

    deleteConfirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteModel();
      }
    });
  }

  deleteModel() {
    this.equipmentService.deleteModelById(this.model.id).subscribe(
      ifSuccess => {
        this.openFeedbackDialog('Equipment model ' + this.model.name + ' has been deleted.');
      },
      err => {
        this.openFeedbackDialog(err.error.message);
      }
    );
  }

  private openFeedbackDialog(message: string) {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: {message: message}
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/equipment']);
      }
    );
  }
}
