import { Component, OnInit } from '@angular/core';
import {Client} from "../../../model/client";
import {ClientService} from "../../../service/client.service";
import {UserService} from "../../../service/user.service";
import {trimJSON} from "../../../util/json/trim";
import {roleNames, User, UserRole} from "../../../model/user";
import {Route, Router} from "@angular/router";
import {JwtStorageService, TokenUserInfo} from "../../../service/token/jwt-storage.service";
import {RoleGuardService} from "../../../service/auth/role-guard.service";
import {WorkerService} from "../../../service/worker.service";

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

  allButtonColor: string = "";
  registeredButtonColor: string = "";
  unregisteredButtonColor: string = "";
  yoursButtonColor: string = "";

  constructor(
    private roleGuardService: RoleGuardService,
    private userService: UserService,
    private workerService: WorkerService,
    private clientService: ClientService,
    private jwtTokenStorageService: JwtStorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.currentUser = this.jwtTokenStorageService.getCurrentUser();
    this.isReceptionist = this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_RECEPTIONIST]);
    this.onAllClick();
  }

  onAllClick() {
    this.allButtonColor = "primary";
    this.registeredButtonColor = "";
    this.unregisteredButtonColor = "";
    this.yoursButtonColor = "";
    this.getAllClients();
  }

  getAllClients(): void {
    this.clients = [];
    this.userService.getAllUndeletedUsers().subscribe(registeredClients => {
        registeredClients = this.deleteCurrentUser(registeredClients);
        this.combineWithUnregisteredClients(registeredClients);
      }
    );
  }
  private combineWithUnregisteredClients(registeredClients: User[]): void {
    registeredClients.forEach(registeredClient => {
      let client = trimJSON(registeredClient,
        ["login", "password", "roles", "services"]);
      this.clients.push(client);
    });
    this.clientService.getAllUnregisteredClients().subscribe(
      unregisteredClients => {
      this.clients = this.clients.concat(unregisteredClients);
    });
  }

  private deleteCurrentUser(registeredClients: User[]): User[] {
    let currentUserId = this.jwtTokenStorageService.getCurrentUser()?.id;
    return registeredClients.filter(client => client.id != currentUserId);
  }

  onRegisteredClick() {
    this.allButtonColor = "";
    this.registeredButtonColor = "primary";
    this.unregisteredButtonColor = "";
    this.yoursButtonColor = "";
    this.getAllRegisteredClients();
  }

  getAllRegisteredClients(): void {
    this.userService.getAllUndeletedUsers().subscribe(
      users => {
        users = this.deleteCurrentUser(users);
        this.clients = users;
      }
    )
  }

  onUnregisteredClick() {
    this.allButtonColor = "";
    this.registeredButtonColor = "";
    this.unregisteredButtonColor = "primary";
    this.yoursButtonColor = "";
    this.getAllUnregisteredClients();
  }

  getAllUnregisteredClients(): void {
    this.clientService.getAllUnregisteredClients().subscribe(clients => {
      this.clients = clients;
    });
  }

  onYoursClick() {
    this.allButtonColor = "";
    this.registeredButtonColor = "";
    this.unregisteredButtonColor = "";
    this.yoursButtonColor = "primary";
    this.getAllWorkerClients();
  }

  getAllWorkerClients(): void {
    this.clients = [];
    let currentWorkerId = this.currentUser?.id;
    // @ts-ignore
    this.workerService.getAllClients(currentWorkerId).subscribe(
      (registeredWorkerClients: User[]) => {

        registeredWorkerClients.forEach(registeredClient => {
          let client = trimJSON(registeredClient, ["login", "password", "roles", "services"]);
          this.clients.push(client);
        });

        // @ts-ignore
        this.workerService.getAllUnregisteredClients(currentWorkerId).subscribe(
          unregisteredClients => {
            this.clients = this.clients.concat(unregisteredClients);
          });
      }
    )
  }

  pickClient(client: Client): void {
    this.clientService.saveSelectedClient(client);
    this.router.navigate(["client/details"]);
  }
}
