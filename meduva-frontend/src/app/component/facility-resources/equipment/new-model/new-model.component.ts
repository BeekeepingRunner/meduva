import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormBuilder,
} from "@angular/forms";
import {ServicesService} from "../../../../service/services.service";
import {RoomService} from "../../../../service/room.service";
import {EquipmentItem} from "../../../../model/equipment";
import {RoomSelectComponent} from "./room-select/room-select.component";
import {EquipmentService} from "../../../../service/equipment.service";
import {Router} from "@angular/router";


export interface NewModelRequest {
  modelName: string,
  itemCount: number,
  servicesIds: number[],
  selectedRoomsIds: number[]
}

@Component({
  selector: 'app-new-model',
  templateUrl: './new-model.component.html',
  styleUrls: ['./new-model.component.css']
})
export class NewModelComponent implements OnInit {

  modelName: string = '';
  isFormValid: boolean = false;

  selectedServicesIds: number[] = [];
  serviceSelectionError: string = '';

  eqItems: EquipmentItem[] = [];
  @ViewChild(RoomSelectComponent)
  private roomSelectComponent!: RoomSelectComponent;
  selectedRoomsIds: number[] = [];
  roomSelectionError: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
    private router: Router,
  ) { }

  ngOnInit(): void {
  }

  onModelNameInput($event: string) {
    this.modelName = $event;
  }

  onItemsGeneration($event: EquipmentItem[]) {
    this.eqItems = $event;
  }

  onModelFormSubmitted($event: boolean) {
    this.isFormValid = $event;
  }

  onServicesSelected($event: number[]) {
    this.selectedServicesIds = $event;
  }

  IsAtLeastOneServiceSelected(): boolean {
    if (this.selectedServicesIds.length > 0) {
      this.serviceSelectionError = '';
      return true;
    } else {
      this.serviceSelectionError = 'You have to select at least one service';
      return false;
    }
  }

  onRoomsSelected($event: Array<number>) {
    this.selectedRoomsIds = $event;
  }

  areAllItemsDisplaced(): boolean {
    let eqItemCount: number = this.eqItems.length;
    if (this.selectedRoomsIds.length == eqItemCount && eqItemCount > 0) {
      this.roomSelectionError = '';
      return true;
    } else {
      this.roomSelectionError = 'You have to dispose all equipment items';
      return false;
    }
  }

  saveModelWithItems() {
    let newModelReuqest: NewModelRequest = {
      modelName: this.modelName,
      itemCount: this.eqItems.length,
      servicesIds: this.selectedServicesIds,
      selectedRoomsIds: this.selectedRoomsIds
    };

    this.equipmentService.saveNewModel(newModelReuqest).subscribe(
      data => {
        this.router.navigate(['/equipment']);
      },
      err => {
        // TODO: do something with error
      }
    );
  }
}
