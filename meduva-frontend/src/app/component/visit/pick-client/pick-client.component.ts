import { Component, OnInit } from '@angular/core';
import {Term, VisitService} from "../../../service/visit.service";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {Client} from "../../../model/client";
import {UserService} from "../../../service/user.service";
import {JwtTokenStorageService} from "../../../service/token/jwt-token-storage.service";

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
    private jwtTokenStorageService: JwtTokenStorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.fetchSelectedTerm();
    // For now, all clients have account
    this.userService.getAllUsers().subscribe(
      clientUsers => {
        this.clients = clientUsers;
        this.deleteCurrentUserFromClients();
      }
    );
  }

  private fetchSelectedTerm() {
    let maybeTerm = this.visitService.getSelectedTerm();
    if (maybeTerm != null) {
      this.selectedTerm = maybeTerm;
    } else {
      // possible error display
    }
  }

  private deleteCurrentUserFromClients() {
    let currentUserId = this.jwtTokenStorageService.getCurrentUser()?.id;
    this.clients = this.clients.filter(client => client.id != currentUserId);
  }

  summarize(client: Client) {
    let selectedTerm = this.visitService.getSelectedTerm();
    if (selectedTerm != null) {
      selectedTerm.clientId = client.id;
      this.visitService.saveSelectedTerm(selectedTerm);
      this.router.navigate(['/visit/summary']);
    }
  }
}
