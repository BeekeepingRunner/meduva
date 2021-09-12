import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getAllServices(): Observable<Service[]> {
    return this.httpClient.get<Service[]>(environment.API_BASE_URL + 'api/service/all');
  }
}

export interface Service {
  id: number,
  name: string,
  description: string,
  durationInMin: number,
  price: number,
  deleted: boolean
}
