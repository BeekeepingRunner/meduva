import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Service} from "../../../model/service";
import {ActivatedRoute} from "@angular/router";
import {ServicesService} from "../../../service/services.service";
import {RoomService} from "../../../service/room.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";

export interface Item{
   selected: boolean;
   service: Service;

}

@Component({
  selector: 'app-edit-performed-services',
  templateUrl: './edit-performed-services.component.html',
  styleUrls: ['./edit-performed-services.component.css']
})
export class EditPerformedServicesComponent implements OnInit, AfterViewInit {

  @ViewChild((MatSort)) sort!: MatSort;

  roomId!: number;
  itemlessServices: Service[] = [];
  performedServices: Service[] = [];
  displayedColumns: string[] = ['service.name'];
  items: Item[] = [];
  itemsDataSource!: MatTableDataSource<any>;

  constructor(
    private route: ActivatedRoute,
    private servicesService: ServicesService,
    private roomService: RoomService
  ) { }

  ngOnInit(): void {
    this.getAllItemlessServices();
  }

  ngAfterViewInit() {
    this.itemsDataSource = new MatTableDataSource(this.items);
    this.itemsDataSource.sortingDataAccessor = (item, property ) => {
      switch(property){
        case 'service.name': return item.service.name;
      }
    }
    this.itemsDataSource.sort = this.sort;
    console.log(this.itemsDataSource)
  }

  //wszystkie itemless
  getAllItemlessServices(){
    this.roomId = this.route.snapshot.params.id;

    this.servicesService.getAllItemless().subscribe(
      services => {
        this.itemlessServices = services;
        this.getPerformedServices();
        console.log(this.itemlessServices);
      }
    );
  }

  //wszystkie wykonywane w sali
  getPerformedServices(){
    this.roomService.getRoomServices(this.roomId).subscribe(
      performedServices => {
        this.performedServices = performedServices;
          this.fillCheckboxes();
        console.log(this.performedServices);
      }
    )
  }

  //zrobienie listy ze wszystkimi itemless servicami i
  //zaznaczenie pola selected dla tych które powtarzają się w performed
  fillCheckboxes(){
    for(let itemlessService of this.itemlessServices){
      let item: Item = {
        selected: false,
        service: itemlessService
      }
      this.items.push(item)
      //this.items.push(new Item(false, service));
    }



    for(let item of this.items){
      for(let performedService of this.performedServices){
        console.log(item.service);
        console.log(performedService);
        if(item.service?.id === performedService.id){
          item.selected = true;
          break;
        }
      }
    }
    console.log(this.items);
  }
}
