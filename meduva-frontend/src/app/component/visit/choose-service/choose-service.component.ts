import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";

@Component({
  selector: 'app-choose-service',
  templateUrl: './choose-service.component.html',
  styleUrls: ['./choose-service.component.css']
})
export class ChooseServiceComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];
  generatingTerms: boolean = false;

  constructor(
    private servicesService: ServicesService,
  ) { }

  ngOnInit(): void {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      }
    )
  }

  getTermsForService(serviceId: number): void {
    this.generatingTerms = true;
    this.servicesService.getTermsForService(serviceId).subscribe(
      data => {
        console.log(data);
        this.generatingTerms = false;
      },
      err => {
        console.log(err);
        this.generatingTerms = false;
      }
    );
  }
}
