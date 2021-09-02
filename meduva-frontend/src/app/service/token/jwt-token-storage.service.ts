import { Injectable } from '@angular/core';
import {Role} from "../user.service";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class JwtTokenStorageService {

  constructor() { }

  signOut() : void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getCurrentUser(): TokenUser | null {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    } else {
      return null;
    }
  }
}

export interface TokenUser {
  accessToken: string,
  tokenType: string,
  refreshToken: string,
  id: number,
  login: string,
  email: string,
  roles: Role[]
}
