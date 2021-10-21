import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Client} from "../model/client";
import {Service} from "../model/service";

const SELECTED_CLIENT_KEY: string = "SELECTED_CLIENT";

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

  saveSelectedClient(client: Client) : void {
    window.sessionStorage.setItem(SELECTED_CLIENT_KEY, JSON.stringify(client));
  }

  getSelectedClient() : Client | null {
    let clientJSON: string | null = window.sessionStorage.getItem(SELECTED_CLIENT_KEY);
    if (clientJSON) {
      return JSON.parse(clientJSON);
    } else {
      return null;
    }
  }

  addClient(client: any): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + "api/unregistered-client/add", client);
  }
}
