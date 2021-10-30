import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {
  FormBuilder,
} from "@angular/forms";
import {ServicesService} from "../../../../service/services.service";
import {RoomService} from "../../../../service/room.service";
import {EquipmentItem} from "../../../../model/equipment";
/**import {RoomSelectCreatorComponent} from "./room-select-creator/room-select-creator.component";*/
import {EquipmentService} from "../../../../service/equipment.service";
import {Router} from "@angular/router";
import {NewModelComponent} from "../../../equipment/new-model/new-model.component";
import {Room} from "../../../../model/room";


export interface NewModelRequest {
  modelName: string,
  itemCount: number,
  servicesIds: number[],
  selectedRoomsIds: number[]
}

@Component({
  selector: 'app-new-model-creator',
  templateUrl: './new-model-creator.component.html',
  styleUrls: ['./new-model-creator.component.css']
})
export class NewModelCreatorComponent extends NewModelComponent implements OnInit {

  modelName: string = '';
  isFormValid: boolean = false;

  selectedServicesIds: number[] = [];
  serviceSelectionError: string = '';

  @Input() roomItems: Room[] = [];
  /**Because of the fact that this class is used in two cases, during adding the room and in the creator, there is an input annotation.
   Creator uses that class in the string of giving data among the dialogs and classes*/


  eqItems: EquipmentItem[] = [];
 /** @ViewChild(RoomSelectCreatorComponent)
  private roomSelectComponent!: RoomSelectCreatorComponent;*/
  selectedRoomsIds: number[] = [];
  roomSelectionError: string = '';


  constructor(
     formBuilder: FormBuilder,
     servicesService: ServicesService,
     roomService: RoomService,
     equipmentService: EquipmentService,
     router: Router,
  ) {
    super(formBuilder,servicesService,roomService,equipmentService,router)
  }

  ngOnInit(): void {
  }


//zamiast zapisywania do bazy należy zrobic zapis do tabeli w kreatorze
  /*saveModelWithItems() {
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
  }*/

}
