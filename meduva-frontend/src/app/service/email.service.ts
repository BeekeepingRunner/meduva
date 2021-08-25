import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private http: HttpClient) { }

  sendTestMail() {
    return this.http.get(environment.API_BASE_URL + 'api/password/mail-test');
  }
}
