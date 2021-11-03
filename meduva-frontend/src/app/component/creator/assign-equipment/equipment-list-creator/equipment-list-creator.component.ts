import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {EquipmentItem, EquipmentModel} from "../../../../model/equipment";
import {EquipmentService} from "../../../../service/equipment.service";
import {ConfigureRoomsCreatorDialogComponent} from "../../../dialog/configure-rooms-creator-dialog/configure-rooms-creator-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ConfigureEquipmentCreatorDialogComponent} from "../../../dialog/configure-equipment-creator-dialog/configure-equipment-creator-dialog.component";
import {Room} from "../../../../model/room";

@Component({
  selector: 'app-equipment-list-creator',
  templateUrl: './equipment-list-creator.component.html',
  styleUrls: ['./equipment-list-creator.component.css']
})
export class EquipmentListCreatorComponent implements OnInit {

  @Input() roomItems!: Room[];
  models: EquipmentModel[] = [];
  modelTableColumns: string[] = ['name'];
  itemTableColumns: string[] = ['name', 'room', 'status'];

  @Output() eqModelEmitter = new EventEmitter<EquipmentModel[]>();

  constructor(
    private equipmentService: EquipmentService,
    public dialog: MatDialog,
  ) {
  }

  ngOnInit(): void {
   // this.getModels();
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

  openConfigureEquipmentCreatorDialog() {
    const equipmentCreatorDialogRef = this.dialog.open(ConfigureEquipmentCreatorDialogComponent,{
      data: { roomItems: this.roomItems }
    });
    equipmentCreatorDialogRef.afterClosed().subscribe(equipmentModel => {
      let eqModel: EquipmentModel = equipmentModel;
      this.models.push(eqModel)
      this.eqModelEmitter.emit(this.models);

    });

  }
}
