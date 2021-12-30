import {AfterViewInit, Component, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {JwtStorageService, TokenUserInfo} from "./service/token/jwt-storage.service";
import {MatSidenav} from "@angular/material/sidenav";
import {BreakpointObserver} from "@angular/cdk/layout";
import {Role, roleNames, User, UserRole} from "./model/user";
import {ClientService} from "./service/client.service";
import {Client} from "./model/client";
import {UserService} from "./service/user.service";
import {RoleGuardService} from "./service/auth/role-guard.service";
import {Router} from "@angular/router";
import {VisitService} from "./service/visit.service";
import {Subscription} from "rxjs";
import {EventBusService} from "./_shared/event-bus.service";
import {EventData} from "./_shared/event.class";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit, OnChanges, OnDestroy {

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isLoggedIn = false;
  pageTitle = 'Meduva';

  currentUser!: User;

  showClientOptions = false;
  showWorkerOptions = false;
  showReceptionistOptions = false;
  showAdminPanel = false;

  eventBusSub?: Subscription;

  constructor(
    private observer: BreakpointObserver,
    private tokenStorageService: JwtStorageService,
    private roleGuardService: RoleGuardService,
    private userService: UserService,
    private eventBusService: EventBusService,
  ) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.userService.getUserDetails(1).subscribe(
      data => {

      }, err => {
        if (err.status === 403)
          this.eventBusService.emit(new EventData('logout', null));
      }
    );
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    if (this.isLoggedIn) {
      // @ts-ignore
      this.userService.getUserDetails(this.tokenStorageService.getCurrentUser()?.id).subscribe(
        user => {
          this.currentUser = user;
          this.pageTitle = this.currentUser.name;
          this.setVisibleOptions();

          this.eventBusSub = this.eventBusService.on('logout', () => {
            this.logout();
          });
        }
      );
    }
  }

  private setVisibleOptions(): void {
    this.showClientOptions = this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_CLIENT]);
    this.showWorkerOptions = this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_WORKER]);
    this.showReceptionistOptions = this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_RECEPTIONIST]);
    this.showAdminPanel = this.roleGuardService.hasCurrentUserExpectedRole(roleNames[UserRole.ROLE_ADMIN]);
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

  ngOnDestroy() {
    if (this.eventBusSub) {
      this.eventBusSub.unsubscribe();
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.isLoggedIn = false;
    this.unsetVisibleOptions();
    window.location.reload();
  }

  private unsetVisibleOptions(): void {
    this.showClientOptions = false;
    this.showWorkerOptions = false;
    this.showReceptionistOptions = false;
    this.showAdminPanel = false;
  }
}
