import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Service} from "../../../model/service";
import {ActivatedRoute} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {RoomService} from "../../../service/room.service";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-edit-performed-services',
  templateUrl: './edit-performed-services.component.html',
  styleUrls: ['./edit-performed-services.component.css']
})
export class EditPerformedServicesComponent implements OnInit {

  roomId!: number;
  itemlessServices: Service[] = [];
  performedServices: Service[] = [];
  displayedColumns: string[] = ['name', 'action'];

  constructor(
    private route: ActivatedRoute,
    private servicesService: ServicesService,
    private roomService: RoomService
  ) { }

  ngOnInit(): void {
    this.getAllItemlessServices();
  }

  //wszystkie itemless
  getAllItemlessServices(){
    this.roomId = this.route.snapshot.params.id;

    this.servicesService.getAllItemless().subscribe(
      services => {
        this.itemlessServices = services;
        console.log(this.itemlessServices);
        this.getPerformedServices();
      }
    );
  }

  //wszystkie wykonywane w sali
  getPerformedServices(){
    this.roomService.getRoomServices(this.roomId).subscribe(
      performedServices => {
        this.performedServices = performedServices;
        console.log(this.performedServices);
      }
    )
  }

  isServicePerformedInRoom(service: Service): boolean {
    for (let performedService of this.performedServices) {
      if (service.id == performedService.id) {
        return true;
      }
    }
    return false;
  }
}
