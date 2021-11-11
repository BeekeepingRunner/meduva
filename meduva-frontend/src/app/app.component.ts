import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
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

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isLoggedIn = false;

  pageTitle = 'Meduva';

  currentUser!: User;
  userRoles: UserRole[] = [];

  showClientOptions = false;
  showWorkerOptions = false;
  showReceptionistOptions = false;
  showAdminPanel = false;

  constructor(
    private observer: BreakpointObserver,
    private tokenStorageService: JwtStorageService,
    private roleGuardService: RoleGuardService,
    private userService: UserService,
    private clientService: ClientService,
    private visitService: VisitService,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    if (this.isLoggedIn) {
      // @ts-ignore
      this.userService.getUserDetails(this.tokenStorageService.getCurrentUser()?.id).subscribe(
        user => {
          this.currentUser = user;
          this.setVisibleOptions();
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

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
  }

  onAppointmentMaking(): void {
    this.visitService.clearAllVisitData();
    this.saveUserAsClient();
    this.router.navigate(['/visit/pick-service']);
  }

  private isClient(): boolean {
    return !this.userRoles.includes(UserRole.ROLE_WORKER);
  }

  private saveUserAsClient(): void {
    let currUserAsClient: Client = {
      id: this.currentUser.id,
      name: this.currentUser.name,
      surname: this.currentUser.surname,
      phoneNumber: this.currentUser.phoneNumber,
      email: this.currentUser.email
    };
    this.visitService.saveSelectedClient(currUserAsClient);
  }

  onVisitPlanning() {
    this.visitService.clearAllVisitData();
    this.router.navigate(['/visit/pick-client']);
  }
}
