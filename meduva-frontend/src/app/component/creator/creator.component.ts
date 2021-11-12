import {Component, OnInit, Output, ViewChild} from '@angular/core';
import {
  FormBuilder,
} from "@angular/forms";
import {ServicesService} from "../../service/services.service";
import {RoomService} from "../../service/room.service";
import {EquipmentItem, EquipmentModel} from "../../model/equipment";
import {RoomSelectComponent} from "../facility-resources/equipment/new-model/room-select/room-select.component";
import {EquipmentService} from "../../service/equipment.service";
import {Router} from "@angular/router";
import {Room} from "../../model/room";
import {EquipmentListCreatorComponent} from "./assign-equipment/equipment-list-creator/equipment-list-creator.component";
import {Service} from "../../model/service";
import {ConfigureServicesCreatorDialogComponent} from "../dialog/configure-services-creator-dialog/configure-services-creator-dialog.component";
import {ConfirmationDialogComponent} from "../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {NewModelRequest} from "../facility-resources/equipment/new-model/new-model.component";
import {FeedbackDialogComponent} from "../dialog/feedback-dialog/feedback-dialog.component";
import {CreatorService} from "../../service/creator.service";


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

  @Output() roomItems: Room[] = [];
  eqModels: EquipmentModel[] = [];
  services: Service[] = [];

  roomSelectionError: string = '';

  /** Implementation of NewModelRequest requirements*/
  itemCount!: number;
  modelName!: string;
  selectedRoomsIds!: number[];
  servicesIds!: number[];

  private allServicesIds: Map<string, number>;
  private allRoomsIds: Map<string, number>;

  constructor(
    private formBuilder: FormBuilder,
    private servicesService: ServicesService,
    private roomService: RoomService,
    private equipmentService: EquipmentService,
    private creatorService: CreatorService,
    private router: Router,
    public dialog: MatDialog,
  ) {
    this.allServicesIds = new Map();
    this.allRoomsIds = new Map();
  }

  ngOnInit(): void {
    this.roomService.getAllUndeletedRooms().subscribe(roomsForGetRoomNames => {
      this.roomsFromDB = roomsForGetRoomNames;
    });

    const clearDatabaseDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do u want to clear your previous configuration? Attention! The change cannot be undone' }
    });

    clearDatabaseDialogRef.afterClosed().subscribe(confirmed => {
      if(confirmed){
        this.creatorService.deleteAllConfiguration().subscribe();
      }
    });
  }

  onRoomsGeneration($event: Room[]) {
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

  addServicesToDatabaseAndGetIds() {
    this.servicesService.getAllUndeletedServices().subscribe(data => {
      for(let anyService of data){
        if(anyService.id!=undefined)
        this.allServicesIds.set(anyService.name, anyService.id);
      }
    })

    for(let service of this.services){
      this.servicesService.addNewService(service).subscribe(
        data => {
          if(data.id!=undefined)
          this.allServicesIds.set(data.name, data.id);
        }
      );
    }
  }

  addRoomsToDatabaseAndGetIds(){
    for(let room of this.roomItems){
      if(room.id==undefined){
        this.roomService.addNewRoom(room).subscribe(
          data => {
            if(data.id!=undefined)
            this.allRoomsIds.set(data.name, data.id);
          }
        );
      }
      else{
        this.allRoomsIds.set(room.name,room.id)
      }
    }
  }

  linkServicesAndRooms() {
    for(let room of this.roomItems){
      room.id = this.allRoomsIds.get(room.name)
      if(room.id && room.services){
        let roomServicesToAdd: Service[] = [];
        for(let anyRoomService of room.services){
          for(let serviceToAssign of this.services){
            if(anyRoomService.name==serviceToAssign.name){
              anyRoomService.id=this.allServicesIds.get(anyRoomService.name);
              roomServicesToAdd.push(anyRoomService);
            }
          }
        }

        this.roomService.editServices(room.id, roomServicesToAdd).subscribe()
      }
    }
  }

  addAndLinkEquipment() {
    for(let model of this.eqModels){

      let modelServicesIds: number[] = [];
      let modelRoomsIds: number[] = [];

      for(let modelService of model.services){
        let modelId = this.allServicesIds.get(modelService.name);
        if(modelId!=undefined)
        modelServicesIds.push(modelId);
      }

      for(let modelEquipment of model.items){
        if(modelEquipment.room?.name!=undefined){
          let roomId = this.allRoomsIds.get(modelEquipment.room?.name);
          if(roomId!=undefined)
          modelRoomsIds.push(roomId)
        }
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
        this.equipmentService.saveModelConnections(modelRequest).subscribe();
      }
    }
  }

  saveConfigurationInDatabase() {

    this.allServicesIds = new Map();
    this.allRoomsIds = new Map();

    this.addServicesToDatabaseAndGetIds();
    this.addRoomsToDatabaseAndGetIds();

    const confirmConfigurationDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Configuration has saved' }
    });

    confirmConfigurationDialogRef.afterClosed().subscribe(confirmed => {

      this.linkServicesAndRooms();
      this.addAndLinkEquipment();

    });

    this.router.navigate(['/home']);
  }
}
