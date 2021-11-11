import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Service} from "../../../../model/service";
import {ServicesService} from "../../../../service/services.service";
import {EquipmentModel} from "../../../../model/equipment";
import {RoomListComponent} from "../../../rooms/room-list/room-list.component";
import {Room} from "../../../../model/room";

@Component({
  selector: 'app-rooms-select-service-creator',
  templateUrl: './rooms-select-service-creator.component.html',
  styleUrls: ['./rooms-select-service-creator.component.css']
})
export class RoomsSelectServiceCreatorComponent extends RoomListComponent {

  @Output() relatedRoomsEmitter = new EventEmitter<Room[]>();

  @Input() creatorRooms: Room[] = [];
  @Input() creatorModels: EquipmentModel[] = [];
  @Input() service!: Service;

  @Input() selectedModels: EquipmentModel[] = [];

  selectedRooms: Room[] = [];
  roomsToSelect: Room[] = []
  roomsToUncheck: string[] = [];

  automatically : boolean = true;

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;


  ngOnInit(): void {
    this.getAllRoomsAssociatedWithSelectedServices();
    this.fetchRoomsFromDatabaseAndMergeWithRoomsFromCreator();
    this.getRoomsNotAssosciatedWithAnyEquipment();
  }

  getAllRoomsAssociatedWithSelectedServices() {

    let uniqueRoomNames: string[] = [];
    let uniqueRooms = new Set<Room>();
    for(let model of this.selectedModels){
        let eqItems = model.items;
        for(let item of eqItems){
          if(item.room)
          uniqueRooms.add(item.room);
        }
    }

    for(let room of uniqueRooms){
      if(uniqueRoomNames.includes(room.name)){
        uniqueRooms.delete(room)
      }
      else{
        uniqueRoomNames.push(room.name)
      }
    }

    this.roomsToSelect=Array.from(uniqueRooms.values());

  }


  fetchRoomsFromDatabaseAndMergeWithRoomsFromCreator() {
    this.roomService.getAllUndeletedRooms().subscribe(
      rooms => {
        this.rooms=rooms;
        for(let creatorRoom of this.creatorRooms){
          this.rooms.unshift(creatorRoom);
        }
      }
    );
  }

  getRoomsNotAssosciatedWithAnyEquipment(){

    let uniqueRoomNames: string[] = [];
    for(let model of this.creatorModels){
      let eqItems = model.items;
      for(let item of eqItems){
        if(item.room && !uniqueRoomNames.includes(item.room.name))
          uniqueRoomNames.push(item.room.name);
      }
    }
    this.roomsToUncheck = uniqueRoomNames;
  }

  assignRoomsToService() {

    if(!this.automatically){
      for (let selectedRoom of this.selectedRooms){
        for(let properRoom of this.rooms){
          if(selectedRoom.name==properRoom.name){
            if(!properRoom.services?.includes(this.service))
              properRoom.services?.push(this.service);
          }
        }
      }
      this.relatedRoomsEmitter.emit(this.rooms);
    }

    else{
      for(let anyRoom of this.rooms){
        if(!this.roomsToUncheck.includes(anyRoom.name)){
          if(!anyRoom.services?.includes(this.service))
            anyRoom.services?.push(this.service);
        }
      }
      this.relatedRoomsEmitter.emit(this.rooms);
    }

  }

  changeAutoState(){
    if(this.automatically)
      this.automatically=false;
    else
      this.automatically=true;
  }

  ifRoomIsAssignedToAnyEquipment(room: Room) {
    if(this.roomsToUncheck.includes(room.name))
      return false;
    else
      return true;
  }
}
