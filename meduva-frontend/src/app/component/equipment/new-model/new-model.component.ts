import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from "@angular/forms";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {Room} from "../../../model/room";
import {RoomService} from "../../../service/room.service";
import {EquipmentItem} from "../../../model/equipment";
import {RoomSelectComponent} from "./room-select/room-select.component";
import {EquipmentService} from "../../../service/equipment.service";
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

  modelFormGroup!: FormGroup;
  modelNameAvailable: boolean = false;
  modelNotAvailableErr: string = '';

  services: Service[] = [];
  selectedServices: Service[] = [];
  servicesIds: number[] = [];
  serviceSelectionError: string = '';

  eqItems: EquipmentItem[] = [];
  rooms: Room[] = [];
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
    this.modelFormGroup = this.formBuilder.group({
      modelName : new FormControl('', [
        Validators.required
      ]),
      itemCount: new FormControl('', [
        Validators.required,
        Validators.min(1),
      ])
    });
  }

  setModelAvailability(): void {
    let modelName = this.modelFormGroup.controls.modelName.value;

    this.equipmentService.doesModelExistByName(modelName).subscribe(
      doesExist => {
        this.modelNameAvailable = !doesExist;
      },
      err => {
        this.modelNameAvailable = false;
        console.log(err);
      }
    );
  }

  isModelNameAvailable(): boolean {
    if (this.modelNameAvailable) {
      this.modelNotAvailableErr = '';
      return true;
    } else {
      this.modelNotAvailableErr = 'Model with given name already exists';
      return false;
    }
  }

  fetchServices(): void {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
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
  }

  private prepareModelName(modelName: string): string {
    return modelName.split(' ').join('_');
  }

  onServicesSelected($event: Service[]) {
    this.selectedServices = $event;
    console.log(this.selectedServices);
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

  saveModelWithItems() {
    let newModelReuqest: NewModelRequest = {
      modelName: this.modelFormGroup.controls.modelName.value,
      itemCount: this.modelFormGroup.controls.itemCount.value,
      servicesIds: this.servicesIds,
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
