import {Component, OnInit, Output, ViewChild} from '@angular/core';
import {
  FormBuilder,
} from "@angular/forms";
import {ServicesService} from "../../service/services.service";
import {RoomService} from "../../service/room.service";
import {EquipmentItem, EquipmentModel} from "../../model/equipment";
import {RoomSelectComponent} from "../equipment/new-model/room-select/room-select.component";
import {EquipmentService} from "../../service/equipment.service";
import {Router} from "@angular/router";
import {Room} from "../../model/room";
import {EquipmentListCreatorComponent} from "./assign-equipment/equipment-list-creator/equipment-list-creator.component";
import {Service} from "../../model/service";
import {ConfigureServicesCreatorDialogComponent} from "../dialog/configure-services-creator-dialog/configure-services-creator-dialog.component";
import {ConfirmationDialogComponent} from "../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";


export interface CreatorRequest {
  modelName: string,
  itemCount: number,
  servicesIds: number[],
  selectedRoomsIds: number[]
}

@Component({
  selector: 'app-creator',
  templateUrl: './creator.component.html',
  styleUrls: ['./creator.component.css']
})
export class CreatorComponent implements OnInit {

  isFormValid: boolean = false;

  @Output() roomItems: Room[] = [];
  eqModels: EquipmentModel[] = [];
  services: Service[] = [];

  private roomSelectComponent!: RoomSelectComponent;
  roomSelectionError: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
    private router: Router,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {

  }

  onItemsGeneration($event: Room[]) {
    this.roomItems = $event;
  }

  onEquipmentModelGot($event: EquipmentModel[]){
    this.eqModels = $event;
  }

  onServicesGot($event: Service[]){
    this.services = $event;
  }

  onModelFormSubmitted($event: boolean) {
    this.isFormValid = $event;
  }

  areAllItemsDisplaced(): boolean {

    if (this.roomItems.length > 0) {
      for(let anyRoom of this.roomItems){
        if(anyRoom.name == '' || anyRoom.name == null){
          this.roomSelectionError = 'Room name cannot be empty';
          return false;
        }
      }
      this.roomSelectionError = '';

      return true;

    } else {
      this.roomSelectionError = 'You must add minimum one room';
      return false;
    }

  }

  saveConfigurationInDatabase() {
    console.log(this.roomItems);
    console.log(this.eqModels);
    console.log(this.services);

    const confirmConfigurationDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Are u sure you want to save that configuration?' }
    });

    confirmConfigurationDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        console.log("Hurra")
      }
    });
  }
}
