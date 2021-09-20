import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";

export interface ItemRoom {
  itemId: number,
  roomId: number
}

export interface NewModelRequest {
  modelName: string,
  itemCount: number,
  servicesIds: number[],
  itemRooms: ItemRoom[]
}

@Component({
  selector: 'app-new-model',
  templateUrl: './new-model.component.html',
  styleUrls: ['./new-model.component.css']
})
export class NewModelComponent implements OnInit {

  modelFormGroup!: FormGroup;
  secondFormGroup: any;
  isLinear: boolean = true;

  services: Service[] = [];
  selectedServices: Service[] = [];
  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  servicesIds: number[] = [];
  serviceSelectionError: string = '';

  itemNames: string[] = [];
  rooms: Room[] = [];
  selectedRooms: Room[] = [];

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

  createItemNames(): void {
    let itemCount: number = this.modelFormGroup.controls.itemCount.value;
    let modelName: string = this.modelFormGroup.controls.modelName.value;
    for(let i = 1; i <= itemCount; i++) {
      this.itemNames.push(modelName + '_' + i);
    }
    console.log(this.itemNames);
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

  // TODO:
  //  1. check if model with given name already exists
  saveModelWithItems() {

    // TODO: populate arrays with appropriate key-value pairs
    let itemRooms: ItemRoom[] = [];
    let servicesIds: number[] = [];

    let newModelReuqest: NewModelRequest = {
      modelName: this.modelFormGroup.controls.modelName.value,
      itemCount: this.modelFormGroup.controls.itemCount.value,
      servicesIds: servicesIds,
      itemRooms: itemRooms
    };

    // TODO: send request
  }
}
