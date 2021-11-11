import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {Client} from "../../../../model/client";
import {VisitService} from "../../../../service/visit.service";
import {UserService} from "../../../../service/user.service";
import {JwtStorageService} from "../../../../service/token/jwt-storage.service";
import {ClientService} from "../../../../service/client.service";
import {User} from "../../../../model/user";
import {trimJSON} from "../../../../util/json/trim";

@Component({
  selector: 'app-client-selection',
  templateUrl: './client-selection.component.html',
  styleUrls: ['./client-selection.component.css']
})
export class ClientSelectionComponent implements OnInit {

  clients: Client[] = [];
  displayedColumns: string[] = ["name", "surname", "phoneNumber", "email"];

  @Output()
  clientEmitter = new EventEmitter<Client>();

  constructor(
    private visitService: VisitService,
    private userService: UserService,
    private jwtTokenStorageService: JwtStorageService,
    private clientService: ClientService,
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
    this.clientEmitter.emit(client);
  }
}
