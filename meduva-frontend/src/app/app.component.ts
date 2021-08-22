import {Component, OnInit, ViewChild} from '@angular/core';
import {TokenStorageService} from "./service/token-storage.service";
import {MatSidenav} from "@angular/material/sidenav";
import {BreakpointObserver} from "@angular/cdk/layout";
import {UserService} from "./service/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isLoggedIn = false;
  // showAdminBoard = false;
  // showModeratorBoard = false;
  fullName?: string;

  constructor(
    private observer: BreakpointObserver,
    private tokenStorageService: TokenStorageService,
    private userService: UserService,
    ) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
        let currentUser = this.tokenStorageService.getCurrentUser();
        this.userService.getUserDetails(currentUser.id).subscribe(
          data => {
            this.fullName = data.name + ' ' + data.surname;
          }
        );

      //this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      //this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');
    }
  }

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
