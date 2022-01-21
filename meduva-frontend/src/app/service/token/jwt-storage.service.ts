import { Injectable } from '@angular/core';
import {Role} from "../../model/user";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../environments/environment";

const TOKEN_KEY = 'auth-token';
const REFRESH_TOKEN_KEY = 'auth-refresh-token';
const USER_KEY = 'auth-user';

// Enables to store logged in user data in sessionStorage
@Injectable({
  providedIn: 'root'
})
export class JwtStorageService {

  constructor(
    private http: HttpClient,
  ) { }

  signOut() : void {
    window.localStorage.clear();
  }

  public saveToken(token: string): void {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, token);
    const user = this.getCurrentUser();
    if (user != null && user.id) {
      this.saveUser({
        ...user,
        accessToken: token
      });
    }
  }

  public saveRefreshToken(token: string): void {
    window.localStorage.removeItem(REFRESH_TOKEN_KEY);
    window.localStorage.setItem(REFRESH_TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.localStorage.getItem(TOKEN_KEY);
  }

  public getRefreshToken(): string | null {
    return window.localStorage.getItem(REFRESH_TOKEN_KEY);
  }

  public saveUser(user: TokenUserInfo): void {
    window.localStorage.removeItem(USER_KEY);
    window.localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getCurrentUser(): TokenUserInfo | null {
    const user = window.localStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    } else {
      return null;
    }
  }

  public hasJwtExpired(): boolean {
    const token: string | null = this.getToken();

    if (token != null) {
      let hasExpired: boolean = true;
      this.http.post(environment.API_BASE_URL + 'api/auth/validate-jwt', token).subscribe(
        data => {
          hasExpired = !!data;
        },
        err => {
          console.log(err);
        }
      );
      return hasExpired;
    }
    else {
      return true;
    }
  }
}

export interface TokenUserInfo {
  accessToken: string,
  tokenType: string,
  refreshToken: string,
  id: number,
  login: string,
  email: string,
  roles: Role[]
}
