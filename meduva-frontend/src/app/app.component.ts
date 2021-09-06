import {Component, OnInit, ViewChild} from '@angular/core';
import {JwtTokenStorageService, TokenUserInfo} from "./service/token/jwt-token-storage.service";
import {MatSidenav} from "@angular/material/sidenav";
import {BreakpointObserver} from "@angular/cdk/layout";
import {UserRole} from "./model/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isLoggedIn = false;

  showClientOptions = false;
  showWorkerOptions = false;
  showReceptionistOptions = false;
  showAdminPanel = false;

  constructor(
    private observer: BreakpointObserver,
    private tokenStorageService: JwtTokenStorageService,
  ) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    if (this.isLoggedIn) {
      this.setVisibleContent();
    }
  }

  private setVisibleContent(): void {
    let currentUser: TokenUserInfo | null = this.tokenStorageService.getCurrentUser();
    let roles: UserRole[] = this.fetchRoles(currentUser);
    this.setVisibleOptionsFor(roles);
  }

  private fetchRoles(currentUser: TokenUserInfo | null): UserRole[] {

    let roles: UserRole[] = [];
    currentUser!.roles.forEach(role => {
      roles.push(role.id as UserRole);
    });
    return roles;
  }

  private setVisibleOptionsFor(roles: UserRole[]): void {
    this.showClientOptions = roles.includes(UserRole.ROLE_CLIENT);
    this.showWorkerOptions = roles.includes(UserRole.ROLE_WORKER);
    this.showReceptionistOptions = roles.includes(UserRole.ROLE_RECEPTIONIST);
    this.showAdminPanel = roles.includes(UserRole.ROLE_ADMIN);
  }

  // Responsible for closing and opening the side menu based on width of the browser's window
  //
  ngAfterViewInit() {
    if (this.isLoggedIn) {
      this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
        if (res.matches) {
          this.sidenav.mode = 'over';
          this.sidenav.close();
        } else {
          this.sidenav.mode = 'side';
          this.sidenav.open();
        }
      });
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
  }
}
