import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(environment.API_URL + 'all', { responseType : 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(environment.API_URL + 'admin', { responseType : 'text' });
  }

  getUserDetails(userId: number): Observable<User> {
    return this.http.get<User>(environment.API_BASE_URL + 'api/user/find/' + userId);
  }

  // Returns the most significant role from list of given roles
  //
  getMasterRole(roles: Role[]) : Role {
    roles.sort((r1, r2) => {
      if (r1.id > r2.id)
        return -1;
      else if (r1.id < r2.id)
        return 1;
      else
        return 0;
    });
    return roles[0];
  }

  getEmailFromResetToken(resetToken: string): Observable<string> {
    return this.http.post<User>(environment.API_BASE_URL + 'api/password/user', resetToken).pipe(
      map(user => user.email)
    );
  }

  resetPassword(requestBody: ResetPasswordRequest): Observable<any> {
    return this.http.post(environment.API_BASE_URL + 'api/password/change', requestBody);
  }
}

export interface User {
  name: string,
  surname: string,
  phoneNumber: string,
  email: string,
  login: string,
  password: string,
  roles: Role[]
}

export interface Role {
  id : number,
  name : string
}

export interface ResetPasswordRequest {
  resetToken: string,
  password: string,
  repeatPassword: string
}
