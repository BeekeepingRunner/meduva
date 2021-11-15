import {Component, Input, OnInit} from '@angular/core';
import {Client} from "../../../../model/client";
import {Visit} from "../../../../model/visit";
import {VisitService} from "../../../../service/visit.service";

@Component({
  selector: 'app-client-visits',
  templateUrl: './client-visits.component.html',
  styleUrls: ['./client-visits.component.css']
})
export class ClientVisitsComponent implements OnInit {

  @Input()
  client!: Client;

  visits: Visit[] = [];
  displayedColumns: string[] = ['date', 'hour', 'serviceName', 'worker', 'room', 'status'];

  constructor(
    private visitService: VisitService,
  ) { }

  ngOnInit(): void {
    if (this.client.email) {
      this.getRegisteredClientVisits();
    } else {
      this.getUnregisteredClientVisits();
    }
  }

  private getRegisteredClientVisits() {
    this.visitService.getAllAsClientByUserId(this.client.id).subscribe(
      visits => {
        this.visits = visits;
        console.log(visits);
      }, err => {
        console.log(err);
      }
    );
  }

  private getUnregisteredClientVisits() {
    this.visitService.getAllOfUnregisteredClient(this.client.id).subscribe(
      visits => {
        this.visits = visits;
        console.log(visits);
      }, err => {
        console.log(err);
      }
    );
  }

}
