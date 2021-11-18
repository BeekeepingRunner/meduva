import { Component, OnInit } from '@angular/core';
import {ClientService} from "../../../service/client.service";
import {Client} from "../../../model/client";
import {FeedbackDialogComponent} from "../../dialog/feedback-dialog/feedback-dialog.component";
import {ConfirmationDialogComponent} from "../../dialog/confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {EquipmentModel} from "../../../model/equipment";
import {Visit} from "../../../model/visit";
import {ConfirmationWithWarningDialogComponent} from "../../dialog/confirmation-with-warning-dialog/confirmation-with-warning-dialog.component";
import {VisitService} from "../../../service/visit.service";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {

  client!: Client;
  clientVisits: Visit[] = [];

  constructor(
    private clientService: ClientService,
    private visitService: VisitService,
    private dialog: MatDialog,
    private router: Router,
  ) { }

  ngOnInit(): void {
    let client: Client | null = this.clientService.getSelectedClient();
    if (client) {
      this.client = client;
    }
  }

  onClientVisitsGot($event: Visit[]){
    this.clientVisits = $event;
  }

  private deleteClient() {
    this.visitService.deleteAllOfUnregisteredClient(this.client.id).subscribe();
    this.clientService.deleteById(this.client.id).subscribe(
      ifSuccess => {
        this.openFeedbackDialog();
      }
    );
  }

  openDeleteConfirmDialog(): void {
      const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: { message: 'Are u sure you want to delete this client?' }
      });

      confirmDialogRef.afterClosed().subscribe(confirmed => {
        if (confirmed) {
          if(this.clientVisits.length>0){
            this.openDeleteWarningDialog();
          }
          else{
            this.deleteClient();
          }
        }
      });
  }

  openDeleteWarningDialog(): void {
    const warningConfirmDialogRef = this.dialog.open(ConfirmationWithWarningDialogComponent, {
      data: { message: 'The customer has visits, if you delete him, you will also delete the visits to which he is assigned!' }
    });

    warningConfirmDialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteClient();
      }
    });
  }

  private openFeedbackDialog() {
    const feedbackDialogRef = this.dialog.open(FeedbackDialogComponent, {
      data: { message: 'Client ' + this.client.name + ' ' + this.client.surname + ' has been deleted.' }
    });

    feedbackDialogRef.afterClosed().subscribe(
      acknowledged => {
        this.router.navigate(['/client/list']);
      }
    );
  }

}
