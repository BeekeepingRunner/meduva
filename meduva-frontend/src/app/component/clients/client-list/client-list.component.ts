import { Component, OnInit } from '@angular/core';
import {Client} from "../../../model/client";
import {ClientService} from "../../../service/client.service";
import {UserService} from "../../../service/user.service";
import {trimJSON} from "../../../util/json/trim";
import {User} from "../../../model/user";
import {Route, Router} from "@angular/router";

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {

  clients: Client[] = [];
  displayedColumns: string[] = ["name", "surname", "phoneNumber", "email"];

  constructor(
    private userService: UserService,
    private clientService: ClientService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.getAllClients();
  }

  getAllClients(): void {
    this.clients = [];
    this.userService.getAllUndeletedUsers().subscribe(
      registeredClients => {
        this.combineWithUnregisteredClients(registeredClients);
      }
    );
  }

  private combineWithUnregisteredClients(registeredClients: User[]): void {

    registeredClients.forEach(registeredClient => {
      let client = trimJSON(registeredClient, ["login", "password", "roles", "services"]);
      this.clients.push(client);
    });

    this.clientService.getAllUnregisteredClients().subscribe(unregisteredClients => {
      this.clients = this.clients.concat(unregisteredClients);
    });
  }

  getAllRegisteredClients(): void {
    this.userService.getAllUndeletedUsers().subscribe(
      users => {
        this.clients = users;
      }
    )
  }

  getAllUnregisteredClients(): void {
    this.clientService.getAllUnregisteredClients().subscribe(clients => {
      this.clients = clients;
    });
  }

  pickClient(client: Client): void {
    this.clientService.saveSelectedClient(client);
    this.router.navigate(["client/details"]);
  }
}
