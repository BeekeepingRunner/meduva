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

  selectedRooms: Room[] = [];
  automatically : boolean = true;

  @Input() creatorRooms: Room[] = [];
  @Input() creatorModels: EquipmentModel[] = [];
  @Input() service!: Service;

  @Input() selectedModels: EquipmentModel[] = [];

  @Output() relatedRoomsEmitter = new EventEmitter<Room[]>();


  compareFunction = (o1: any, o2: any) => o1.id === o2.id;


  ngOnInit(): void {
    this.getAllRooms()
    console.log(this.creatorModels+"TU DZIALA")
    console.log(this.selectedModels+"heheheheheh")
  }

  getAllRooms() {
    //this.rooms=this.creatorRooms;

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

    this.rooms=Array.from(uniqueRooms.values())
  }

  changeAutoState(){
    if(this.automatically)
    this.automatically=false;
    else
      this.automatically=true;
}

  assignRoomsToService() {
    for (let selectedRoom of this.selectedRooms){
      for(let properRoom of this.rooms){
        if(selectedRoom==properRoom){
          properRoom.services?.push(this.service);
        }
      }
    }
    this.relatedRoomsEmitter.emit(this.rooms);
  }
}
