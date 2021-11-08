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
import {NewModelRequest} from "../equipment/new-model/new-model.component";


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
export class CreatorComponent implements OnInit, NewModelRequest {

  roomsFromDB: Room[] = [];

  isFormValid: boolean = false;
  isRoomsValid: boolean = false;

  @Output() roomItems: Room[] = [];
  eqModels: EquipmentModel[] = [];
  services: Service[] = [];

  roomSelectionError: string = '';

  /** Implementation of NewModelRequest requirements*/
  itemCount!: number;
  modelName!: string;
  selectedRoomsIds!: number[];
  servicesIds!: number[];

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
    private router: Router,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.roomService.getAllUndeletedRooms().subscribe(roomsForGetRoomNames => {
      this.roomsFromDB = roomsForGetRoomNames;
    });

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

    let allServicesIds = new Map();
    let allRoomsIds = new Map();

    this.servicesService.getAllUndeletedServices().subscribe(data => {
      for(let anyService of data){
        allServicesIds.set(anyService.name, anyService.id);
      }
    })

    for(let service of this.services){
      this.servicesService.addNewService(service).subscribe(
        data => {
          allServicesIds.set(data.name, data.id);
        }
      );
    }

    for(let room of this.roomItems){
      if(room.id==undefined){
        this.roomService.addNewRoom(room).subscribe(
          data => {
            allRoomsIds.set(data.name, data.id);
          }
        );
      }
      else{
        allRoomsIds.set(room.name,room.id)
      }
    }


    const confirmConfigurationDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Configuration has saved' }
    });

    confirmConfigurationDialogRef.afterClosed().subscribe(confirmed => {

      if (confirmed) {

        for(let room of this.roomItems){
            room.id = allRoomsIds.get(room.name)
            if(room.id && room.services){
              for(let anyRoomService of room.services){
                anyRoomService.id=allServicesIds.get(anyRoomService.name);
              }
              this.roomService.editServices(room.id, room.services).subscribe()
            }
        }
        for(let model of this.eqModels){

          let modelServicesIds: number[] = [];
          let modelRoomsIds: number[] = [];

          for(let modelService of model.services){
            modelServicesIds.push(allServicesIds.get(modelService.name));
          }
          for(let modelEquipment of model.items){
            modelRoomsIds.push(allRoomsIds.get(modelEquipment.room?.name))
          }
          let modelRequest: NewModelRequest = {
            modelName: model.name,
            itemCount:model.items.length,
            servicesIds:modelServicesIds,
            selectedRoomsIds:modelRoomsIds
          }
          if(model.id==undefined)
          this.equipmentService.saveNewModel(modelRequest).subscribe();
          else{
            //tutaj w przypadku gdy model istnieje to na backendzie wywoluje error 500 bo sa multiply references, dlatego trzeba albo usuwac istniejacy model i dodawac ten, albo zbudowac funkcje na backendzie ktora tylko linkuje model
          }
        }

      }
    });


    this.router.navigate(['/home']);
  }


  checkIfNoRoomExistsInDatabase() {
    let sameRoomNamesCounter: number = 0;
    for(let anyRoom of this.roomItems){
      for (let roomFromDB of this.roomsFromDB){
        if(anyRoom.name==roomFromDB.name){
          sameRoomNamesCounter++;
        }
      }
    }
    if(sameRoomNamesCounter == 0){
      this.roomSelectionError = '';
      return true;
    }
    else{
      this.roomSelectionError = "One of these rooms has already been added";
      return false;
    }


  }
}
