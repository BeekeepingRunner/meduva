import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ResetTokenService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  validateResetToken(resetToken: string): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/password/validate-reset-token',{
      token: resetToken
    });
  }

  validateEmailResetToken(resetToken: string): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/email/validate-email-reset-token', resetToken);
  }
}
