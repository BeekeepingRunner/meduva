import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {EquipmentItem} from "../../../model/equipment";
import {RoomSelectComponent} from "./room-select/room-select.component";
import {EquipmentService} from "../../../service/equipment.service";


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

  modelFormGroup!: FormGroup;

  services: Service[] = [];
  selectedServices: Service[] = [];
  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  servicesIds: number[] = [];
  serviceSelectionError: string = '';
  roomSelectionError: string = '';

  eqItems: EquipmentItem[] = [];
  rooms: Room[] = [];
  @ViewChild(RoomSelectComponent)
  private roomSelectComponent!: RoomSelectComponent;
  selectedRoomsIds: number[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
  ) { }

  ngOnInit(): void {
    this.modelFormGroup = this.formBuilder.group({
      modelName : new FormControl('', [
        Validators.required
      ]),
      itemCount: new FormControl('', [
        Validators.required,
        Validators.min(1)
      ])
    });
  }

  fetchServices(): void {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
        console.log(services);
      },
      err => {
        console.log(err);
      }
    );
  }

  createEquipmentItems(): void {
    let itemCount: number = this.modelFormGroup.controls.itemCount.value;
    if (itemCount > 0) {
      this.generateItems(itemCount);
    }
  }

  private generateItems(itemCount: number): void {
    let modelName: string = this.modelFormGroup.controls.modelName.value;
    modelName = this.prepareModelName(modelName);

    this.eqItems = [];
    for(let i = 1; i <= itemCount; i++)
    {
      let eqItem: EquipmentItem = {
        id: i,
        name: modelName + '_' + i
      }
      this.eqItems.push(eqItem);
    }
    console.log(this.eqItems);
  }

  private prepareModelName(modelName: string): string {
    return modelName.split(' ').join('_');
  }

  saveSelectedServicesIds(): void {
    this.servicesIds = [];
    this.selectedServices.forEach(selectedService => {
      if (selectedService.id != null) {
        this.servicesIds.push(selectedService.id)
      }
    });
  }

  IsAtLeastOneServiceSelected(): boolean {
    if (this.selectedServices.length > 0) {
      this.serviceSelectionError = '';
      return true;
    } else {
      this.serviceSelectionError = 'You have to select at least one service';
      return false;
    }
  }

  fetchRooms() {
    this.roomService.getAllUndeletedRooms().subscribe(
      rooms => {
        this.rooms = rooms;
        console.log(rooms);
      }
    );
  }

  onRoomSelected($event: Array<number>) {
    this.selectedRoomsIds = $event;
  }

  areAllItemsDisplaced(): boolean {
    let eqItemCount: number = this.modelFormGroup.controls.itemCount.value;
    if (this.selectedRoomsIds.length == eqItemCount && eqItemCount > 0) {
      this.roomSelectionError = '';
      return true;
    } else {
      this.roomSelectionError = 'You have to dispose all equipment items';
      return false;
    }
  }

  // TODO:
  //  1. check if model with given name already exists
  saveModelWithItems() {

    let newModelReuqest: NewModelRequest = {
      modelName: this.modelFormGroup.controls.modelName.value,
      itemCount: this.modelFormGroup.controls.itemCount.value,
      servicesIds: this.servicesIds,
      selectedRoomsIds: this.selectedRoomsIds
    };

    console.log(newModelReuqest);
    this.equipmentService.saveNewModel(newModelReuqest).subscribe(
      data => {
        console.log(data);
      }
    );
  }
}
