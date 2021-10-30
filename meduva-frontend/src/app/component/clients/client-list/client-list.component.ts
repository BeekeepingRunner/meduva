import { Component, OnInit } from '@angular/core';
import {Client} from "../../../model/client";
import {ClientService} from "../../../service/client.service";
import {UserService} from "../../../service/user.service";
import {trimJSON} from "../../../util/json/trim";
import {roleNames, User, UserRole} from "../../../model/user";
import {Route, Router} from "@angular/router";
import {JwtStorageService, TokenUserInfo} from "../../../service/token/jwt-storage.service";
import {RoleGuardService} from "../../../service/auth/role-guard.service";

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {

  currentUser!: TokenUserInfo | null;
  clients: Client[] = [];
  displayedColumns: string[] = ["name", "surname", "phoneNumber", "email"];

  isReceptionist: boolean = false;

  constructor(
    private roleGuardService: RoleGuardService,
    private userService: UserService,
    private clientService: ClientService,
    private jwtTokenStorageService: JwtStorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.isReceptionist = this.roleGuardService.hasExpectedRole(roleNames[UserRole.ROLE_RECEPTIONIST]);
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

  getAllRegisteredClients(): void {
    this.userService.getAllUndeletedUsers().subscribe(
      users => {
        users = this.deleteCurrentUser(users);
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
