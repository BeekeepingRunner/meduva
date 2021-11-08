import { Component, OnInit } from '@angular/core';
import {ClientService} from "../../../service/client.service";
import {Client} from "../../../model/client";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {

  client!: Client;

  constructor(
    private clientService: ClientService,
  ) { }

  ngOnInit(): void {
    let client: Client | null = this.clientService.getSelectedClient();
    if (client) {
      this.client = client;
    }
  }

}
