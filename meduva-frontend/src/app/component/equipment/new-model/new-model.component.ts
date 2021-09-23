import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from "@angular/forms";
import {ServicesService} from "../../../service/services.service";
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
