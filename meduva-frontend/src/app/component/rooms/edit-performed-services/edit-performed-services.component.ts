import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Service} from "../../../model/service";
import {ActivatedRoute} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {RoomService} from "../../../service/room.service";
import {MatTableDataSource} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-edit-performed-services',
  templateUrl: './edit-performed-services.component.html',
  styleUrls: ['./edit-performed-services.component.css']
})
export class EditPerformedServicesComponent implements OnInit {

  roomId!: number;
  itemlessServices: Service[] = [];
  performedServices: Service[] = [];
  performedItemlessServices: Service[] = [];
  editedServiceList: Service[] =[];
  displayedColumns: string[] = ['name', 'action'];
  selection?: SelectionModel<Service> = new SelectionModel<Service>();

  editSuccessfully: boolean = false;
  requestSended: boolean = false;
  resultMessage: string = '';


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
        this.getPerformedItemlessServices();
        this.selection = new SelectionModel<Service>(true, this.performedItemlessServices);
      }
    )
  }

  getPerformedItemlessServices(){
    for (let performedService of this.performedServices){
      for(let itemlessService of this.itemlessServices){
        if(performedService.id == itemlessService.id){
          this.performedItemlessServices.push(itemlessService);
        }
      }
    }
  }

  tryEditServices() {
    this.selection!.selected.forEach(s => console.log(s));
    this.selection!.selected.forEach(s => this.editedServiceList.push(s));

    this.roomService.editServices(this.roomId, this.editedServiceList).subscribe(
      data => {
        this.requestSended = true;
        this.editSuccessfully = true;
        this.resultMessage = "Services edited successfully";
      },
      err => {
        this.resultMessage = err.error.message;
        this.requestSended = true;
        this.editSuccessfully = false;
      }
    )
  }
}
