import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Client} from "../model/client";

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  getAllUnregisteredClients() : Observable<Client[]> {
    return this.httpClient.get<Client[]>(environment.API_BASE_URL + 'api/unregistered-client/all/undeleted');
  }
}
