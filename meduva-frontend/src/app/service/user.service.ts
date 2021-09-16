import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {ResetPasswordRequest, Role, User} from "../model/user";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type' : 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(environment.TEST_API_URL + 'all', { responseType : 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(environment.TEST_API_URL + 'admin', { responseType : 'text' });
  }

  getUserDetails(userId: number): Observable<User> {
    return this.http.get<User>(environment.API_BASE_URL + 'api/user/find/' + userId);
  }

  getAllUsers() : Observable<User[]> {
    return this.http.get<User[]>(environment.API_BASE_URL + 'api/user/all');
  }

  getAllUndeletedUsers() : Observable<User[]> {
    return this.http.get<User[]>(environment.API_BASE_URL + 'api/user/all/undeleted');
  }

  getAllWorkers() : Observable<User[]> {
    return this.http.get<User[]>(environment.API_BASE_URL + 'api/user/workers');
  }

  getAllClientsWithAccount() : Observable<User[]> {
    return this.http.get<User[]>(environment.API_BASE_URL + 'api/user/clients');
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

  resetPassword(requestBody: ResetPasswordRequest): Observable<any> {
    return this.http.post(environment.API_BASE_URL + 'api/password/change', requestBody);
  }

  editUser(login: string,
           name: string,
           surname: string,
           phoneNumber: string,
           id: number){

    return this.http.post( environment.API_BASE_URL+"api/user/edit/" + id, {
      login,
      name,
      surname,
      phoneNumber
      }, httpOptions);
  }
}
