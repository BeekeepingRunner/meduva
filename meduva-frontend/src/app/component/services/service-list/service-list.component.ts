import { Component, OnInit } from '@angular/core';
import {ServicesService} from "../../../service/services.service";
import {Service} from "../../../model/service";

@Component({
  selector: 'app-services',
  templateUrl: './service-list.component.html',
  styleUrls: ['./service-list.component.css']
})
export class ServiceListComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];

  constructor(
    protected servicesService: ServicesService
  ) { }

  ngOnInit(): void {
    this.getAllServices();
  }

  getAllServices() {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      }
    )
  }
}
