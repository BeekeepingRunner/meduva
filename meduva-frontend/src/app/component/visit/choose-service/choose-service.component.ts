import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {VisitService} from "../../../service/visit.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-choose-service',
  templateUrl: './choose-service.component.html',
  styleUrls: ['./choose-service.component.css']
})
export class ChooseServiceComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];
  generatingTerms: boolean = false;

  errorMessage: string = '';

  constructor(
    private servicesService: ServicesService,
    private visitService: VisitService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.servicesService.getAllUndeletedServices().subscribe(
      services => {
        this.services = services;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    )
  }

  getTermsForService(service: Service): void {
    this.generatingTerms = true;
    this.visitService.saveSelectedService(service);
    this.router.navigate(['/visit/pick-term']);
  }
}
