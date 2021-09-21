import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {EquipmentItem} from "../../../model/equipment";
import {RoomSelectComponent} from "./room-select/room-select.component";


export interface NewModelRequest {
  modelName: string,
  itemCount: number,
  servicesIds: number[],
  roomIds: number[]
}

@Component({
  selector: 'app-new-model',
  templateUrl: './new-model.component.html',
  styleUrls: ['./new-model.component.css']
})
export class NewModelComponent implements OnInit, AfterViewInit {

  modelFormGroup!: FormGroup;
  isLinear: boolean = true;

  services: Service[] = [];
  selectedServices: Service[] = [];
  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  servicesIds: number[] = [];
  serviceSelectionError: string = '';

  eqItems: EquipmentItem[] = [];
  rooms: Room[] = [];
  @ViewChild(RoomSelectComponent)
  private roomSelectComponent!: RoomSelectComponent;
  selectedRoomIds: number[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
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
    let modelName: string = this.modelFormGroup.controls.modelName.value;

    for(let i = 1; i <= itemCount; i++)
    {
      let eqItem: EquipmentItem = {
        name: modelName + '_' + i,
        room: undefined
      }
      this.eqItems.push(eqItem);
    }
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

  ngAfterViewInit() {

  }

  saveSelectedRooms() {
    this.selectedRoomIds = this.roomSelectComponent.selectedRoomIds;
  }

  // TODO:
  //  1. check if model with given name already exists
  saveModelWithItems() {

    let newModelReuqest: NewModelRequest = {
      modelName: this.modelFormGroup.controls.modelName.value,
      itemCount: this.modelFormGroup.controls.itemCount.value,
      servicesIds: this.servicesIds,
      roomIds: this.selectedRoomIds
    };

    // TODO: send request
  }
}
