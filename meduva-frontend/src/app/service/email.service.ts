import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type' : 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private http: HttpClient) { }

  sendResetLinkMail(email: string) {
    return this.http.post(environment.API_BASE_URL + 'api/password/request', email);
  }

  sendEmailResetLinkMail(id: number, email: string){
    console.log('api/email/request/' + id);
    console.log(email);
    return this.http.post(environment.API_BASE_URL + 'api/email/request/' + id, email);
  }
}
