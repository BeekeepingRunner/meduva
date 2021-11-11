import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {Router} from "@angular/router";
import {Client} from "../../../model/client";
import {UserService} from "../../../service/user.service";
import {JwtStorageService} from "../../../service/token/jwt-storage.service";
import {User} from "../../../model/user";
import {trimJSON} from "../../../util/json/trim";
import {ClientService} from "../../../service/client.service";

@Component({
  selector: 'app-pick-client',
  templateUrl: './pick-client.component.html',
  styleUrls: ['./pick-client.component.css']
})
export class PickClientComponent implements OnInit {

  selectedTerm!: Term;
  clients: Client[] = [];
  displayedColumns: string[] = ["name", "surname", "phoneNumber", "email"];

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private jwtTokenStorageService: JwtStorageService,
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
        registeredClients = this.deleteCurrentUser(registeredClients);
        this.combineWithUnregisteredClients(registeredClients);
      }
    );
  }

  private deleteCurrentUser(registeredClients: User[]): User[] {
    let currentUserId = this.jwtTokenStorageService.getCurrentUser()?.id;
    return registeredClients.filter(client => client.id != currentUserId);
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

  onClientPick(client: Client) {
    this.visitService.saveSelectedClient(client);
    this.router.navigate(['/visit/pick-service']);
  }
}
