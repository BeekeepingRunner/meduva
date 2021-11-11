import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ServicesService} from "../../../../service/services.service";
import {Service} from "../../../../model/service";

@Component({
  selector: 'app-service-selection',
  templateUrl: './service-selection.component.html',
  styleUrls: ['./service-selection.component.css']
})
export class ServiceSelectionComponent implements OnInit {

  services: Service[] = [];

  @Output() serviceEmitter = new EventEmitter<Service>();
  displayedColumns: string[] = ['name', 'duration', 'price'];

  constructor(
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      }, err => {
        console.log(err);
      }
    )
  }

  onServiceSelect(service: Service) {
    this.serviceEmitter.emit(service);
  }
}
