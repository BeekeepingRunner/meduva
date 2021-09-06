import { Injectable } from '@angular/core';
import {CanActivate, Router} from "@angular/router";
import {JwtTokenStorageService} from "../token/jwt-token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    public jwtStorageService: JwtTokenStorageService,
    public router: Router
  ) { }

  canActivate(): boolean {
    if (this.jwtStorageService.getToken() == null || this.jwtStorageService.hasJwtExpired()) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}
