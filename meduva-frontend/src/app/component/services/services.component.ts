import { Component, OnInit } from '@angular/core';
import {Service, ServicesService} from "../../service/services.service";

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
        this.services = services
      },
      err => {
        console.log(err);
      }
    )
  }
}
