import { Component, OnInit } from '@angular/core';
import {ServicesService} from "../../service/services.service";
import {Service} from "../../model/service";

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.css']
})
export class ServicesComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];

  constructor(
    private servicesService: ServicesService
  ) { }

  ngOnInit(): void {
    this.getAllServices();
  }

  getAllServices() {
    this.servicesService.getAllServices().subscribe(
      services => {
        this.services = services;
        console.log(services);
      },
      err => {
        console.log(err);
      }
    )
  }
}
