import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {JwtTokenStorageService, TokenUserInfo} from "../token/jwt-token-storage.service";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type' : 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private jwtTokenStorageService: JwtTokenStorageService,
  ) { }

  login(login: string, password: string): Observable<TokenUserInfo> {
    return this.http.post<TokenUserInfo>(environment.AUTH_API + "signin", {
      login,
      password
    }, httpOptions);
  }

  register(login: string,
           email: string,
           password: string,
           name: string,
           surname: string,
           phoneNumber: string): Observable<any> {

    return this.http.post(environment.AUTH_API + 'signup', {
      login,
      email,
      password,
      name,
      surname,
      phoneNumber
    }, httpOptions);
  }

  public hasJwtExpired(): boolean {
    return !this.jwtTokenStorageService.hasJwtExpired()
  }
}
