import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {ServicesService} from "../../../../service/services.service";
import {RoomService} from "../../../../service/room.service";
import {EquipmentItem, EquipmentModel} from "../../../../model/equipment";
/**import {RoomSelectCreatorComponent} from "./room-select-creator/room-select-creator.component";*/
import {EquipmentService} from "../../../../service/equipment.service";
import {Router} from "@angular/router";
import {NewModelComponent} from "../../../equipment/new-model/new-model.component";
import {Room} from "../../../../model/room";


export interface NewModelRequest {
  modelName: string,
  itemCount: number,
  selectedRoomsIds: number[]
}

@Component({
  selector: 'app-new-model-creator',
  templateUrl: './new-model-creator.component.html',
  styleUrls: ['./new-model-creator.component.css']
})
export class NewModelCreatorComponent extends NewModelComponent implements OnInit {

  @Output() itemsEmitter = new EventEmitter<EquipmentItem[]>();

  @Input() eqModelsFromDB: EquipmentModel[] = [];
  @Input() roomItems: Room[] = [];
  /**Because of the fact that this class is used in two cases, during adding the room and in the creator, there is an input annotation.
   Creator uses that class in the string of giving data among the dialogs and classes*/

  modelName: string = '';
  isFormValid: boolean = false;
  roomSelectionError: string = '';

  eqItems: EquipmentItem[] = [];

  eqModel: EquipmentModel = {
    name: '',
    active: true,
    deleted: false,
    items: [],
    services: []
  };

  selectedRoomsIds: number[] = [];

  constructor(
     formBuilder: FormBuilder,
     servicesService: ServicesService,
     roomService: RoomService,
     equipmentService: EquipmentService,
     router: Router,
  ) {
    super(formBuilder,servicesService,roomService,equipmentService,router)
  }

  ngOnInit() {
    super.ngOnInit();
  }

  onRoomsAssigned($event: EquipmentItem[]) {
    this.eqItems=$event;
    this.eqModel.name=this.modelName;
    this.eqModel.items = this.eqItems;

  }

}
