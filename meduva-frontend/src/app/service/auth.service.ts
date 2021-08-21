import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type' : 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(login: string, password: string): Observable<any> {
    return this.http.post(environment.AUTH_API + "signin", {
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
}
