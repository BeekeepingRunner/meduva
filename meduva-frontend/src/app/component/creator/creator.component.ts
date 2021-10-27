import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormBuilder,
} from "@angular/forms";
import {ServicesService} from "../../service/services.service";
import {RoomService} from "../../service/room.service";
import {EquipmentItem} from "../../model/equipment";
import {RoomSelectComponent} from "../equipment/new-model/room-select/room-select.component";
import {EquipmentService} from "../../service/equipment.service";
import {Router} from "@angular/router";
import {Room} from "../../model/room";


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

  roomItems: Room[] = [];
  @ViewChild(RoomSelectComponent)
  private roomSelectComponent!: RoomSelectComponent;
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


  onItemsGeneration($event: Room[]) {
    this.roomItems = $event;
  }

  onModelFormSubmitted($event: boolean) {
    this.isFormValid = $event;
  }



  areAllItemsDisplaced(): boolean {

    if (this.roomItems.length > 0) {
      for(let anyRoom of this.roomItems){
        if(anyRoom.name == '' || anyRoom.name == null){
          this.roomSelectionError = 'Room name cannot be empty';
        }
      }
      this.roomSelectionError = '';
      return true;

    } else {
      this.roomSelectionError = 'You must add minimum one room';
      return false;
    }

  }


}
