import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(environment.API_URL + 'all', { responseType : 'text' });
  }

  getClientBoard(): Observable<any> {
    return this.http.get(environment.API_URL + 'client', { responseType : 'text' });
  }

  getWorkerBoard(): Observable<any> {
    return this.http.get(environment.API_URL + 'worker', { responseType : 'text' });
  }

  getReceptionistBoard(): Observable<any> {
    return this.http.get(environment.API_URL + 'receptionist', { responseType : 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(environment.API_URL + 'admin', { responseType : 'text' });
  }

  getUserDetails(login: string): Observable<User> {
    return this.http.get<User>(environment.API_BASE_URL + 'users/search/findByLogin?login=' + login);
  }
}

export interface User {
  name: string,
  surname: string,
  phoneNumber: string,
  email: string,
  login: string,
  password: string
}
