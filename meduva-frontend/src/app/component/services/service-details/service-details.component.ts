import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Service} from "../../../model/service";
import {ServicesService} from "../../../service/services.service";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";

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
    private router: Router,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    let serviceId: number = Number(this.route.snapshot.params.id);
    this.servicesService.getById(serviceId).subscribe(
      service => {
        this.service = service;
      }
    );
  }

  openDeleteConfirmDialog(): void {
    const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you want to delete this service?' }
    });

    confirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteService();
      }
    });
  }

  deleteService() {
    this.servicesService.deleteById(this.service.id).subscribe(
      ifSuccess => {
        this.openFeedbackDialog();
      }
    );
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Service ' + this.service.name + ' has been deleted.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/services']);
      }
    );
  }

}
