import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Service} from "../../../../../model/service";
import {ServicesService} from "../../../../../service/services.service";

@Component({
  selector: 'app-services-select',
  templateUrl: './services-select.component.html',
  styleUrls: ['./services-select.component.css']
})
export class ServicesSelectComponent implements OnInit {

  services: Service[] = [];
  selectedServices: Service[] = [];
  @Output() selectedServicesIdsEmmitter = new EventEmitter<number[]>();

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;

  constructor(
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    this.fetchServices();
  }

  fetchServices(): void {
    this.servicesService.getAllNonItemless().subscribe(
      services => {
       this.services=services;
      },
      err => {
        console.log(err);
      }
    );
  }

  emitSelectedServicesIds() {
    let selectedServicesIds: number[] = [];
    this.selectedServices.forEach(service => {
      if (service.id != null) {
        selectedServicesIds.push(service.id);
      }
    })
    this.selectedServicesIdsEmmitter.emit(selectedServicesIds);
  }
}
