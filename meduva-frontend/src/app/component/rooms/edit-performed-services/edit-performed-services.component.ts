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
  editedServiceList: Service[] =[];
  displayedColumns: string[] = ['name', 'action'];
  selection?: SelectionModel<Service>;


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
        this.selection = new SelectionModel<Service>(true, this.itemlessServices);
      }
    )
  }

  isServicePerformedInRoom(service: Service): boolean {
    for (let performedService of this.performedServices) {
      if (service.id == performedService.id) {
        //console.log('checked')
        //this.addOrDeleteServiceList(service);
        return true;
      }
    }
    return false;
  }

  addOrDeleteServiceList(service: Service){
    console.log('checked')
    //console.log(this.editedServiceList)
    if(this.editedServiceList.length > 0){
      for(let editService of this.editedServiceList){
        if(service.id == editService.id ){
          const index = this.editedServiceList.indexOf(editService);
          this.editedServiceList.splice(index, 1);
          //console.log('deleted')
         // console.log(this.editedServiceList)
        }else{
          this.editedServiceList.push(service);
          //console.log('added')
          //console.log(this.editedServiceList)
        }
      }
    }else{
     // console.log('added')
      this.editedServiceList.push(service);
      //console.log(this.editedServiceList)
    }
  }

  addServiceList(){
    console.log(this.editedServiceList)

  }

  logSelection() {
    this.selection!.selected.forEach(s => console.log(s));
  }
}
