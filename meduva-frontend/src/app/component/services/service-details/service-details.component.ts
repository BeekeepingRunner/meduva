import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";

@Component({
  selector: 'app-service-details',
  templateUrl: './service-details.component.html',
  styleUrls: ['./service-details.component.css']
})
export class ServiceDetailsComponent implements OnInit {

  service!: Service;

  constructor(
    private route: ActivatedRoute,
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    let serviceId: number = Number(this.route.snapshot.params.id);
    this.servicesService.getById(serviceId).subscribe(
      service => {
        this.service = service;
      }
    );
  }

  deleteService() {
    this.servicesService.deleteById(this.service.id).subscribe(
      data => {
        console.log(data);
      },
      err => {
        console.log(err);
      }
    );
  }
}
