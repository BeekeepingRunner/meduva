import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {ActivatedRoute} from "@angular/router";
import {ServicesService} from "../../../service/services.service";

@Component({
  selector: 'app-edit-performed-services',
  templateUrl: './edit-performed-services.component.html',
  styleUrls: ['./edit-performed-services.component.css']
})
export class EditPerformedServicesComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'action'];

  constructor(
    private route: ActivatedRoute,
    private servicesService: ServicesService
  ) { }

  ngOnInit(): void {
    this.getAllItemlessServices();
  }

  getAllItemlessServices(){
    let id: number = this.route.snapshot.params.id;

    this.servicesService.getAllItemless().subscribe(
      services => {
        this.services = services;
      }
    );
  }
}
