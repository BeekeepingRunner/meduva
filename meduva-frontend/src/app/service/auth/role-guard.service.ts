import { Injectable } from '@angular/core';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {JwtStorageService, TokenUserInfo} from "../token/jwt-storage.service";
import {Role} from "../../model/user";

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  constructor(
    public auth: AuthService,
    public jwtStorage: JwtStorageService,
    public router: Router,
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRole = route.data.expectedRole;

    if (!this.isUserAuthorizedWith(expectedRole)) {
      this.router.navigate(['access-denied']);
      return false;
    } else
      return true;
  }

  isUserAuthorizedWith(expectedRole: string): boolean {
    return !this.auth.hasJwtExpired() && this.hasExpectedRole(expectedRole);
  }

  hasExpectedRole(expectedRole: string): boolean {
    let user: TokenUserInfo | null = this.jwtStorage.getCurrentUser();
    if (user != null) {
      return this.hasRole(user, expectedRole);
    }
    else return false;
  }

  private hasRole(user: TokenUserInfo, expectedRole: string) {
    let roles: Role[] = user.roles;
    for(let role of roles) {
      if (role.name == expectedRole) {
        return true;
      }
    }
    return false;
  }
}
