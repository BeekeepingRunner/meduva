import { Component, OnInit } from '@angular/core';
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {VisitService} from "../../../service/visit.service";
import {Router} from "@angular/router";
import {ClientService} from "../../../service/client.service";
import {MatDialog} from "@angular/material/dialog";
import {ItemDayDialogComponent} from "../../../schedule/component/dialog/item-day-dialog/item-day-dialog.component";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-choose-service',
  templateUrl: './choose-service.component.html',
  styleUrls: ['./choose-service.component.css']
})
export class ChooseServiceComponent implements OnInit {

  services: Service[] = [];
  displayedColumns: string[] = ['name', 'duration', 'price'];

  errorMessage: string = '';

  constructor(
    private servicesService: ServicesService,
    private visitService: VisitService,
    private dialog: MatDialog,
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

  submitService(service: Service): void {
    this.visitService.saveSelectedService(service);

    const confirmationDialog = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to select a worker?' }
    });

    confirmationDialog.afterClosed().subscribe(clientWantsToSelectWorker => {
      if (clientWantsToSelectWorker) {
        this.router.navigate(['/visit/pick-worker']);
      } else {
        this.router.navigate(['/visit/pick-term']);
      }
    });
  }
}
