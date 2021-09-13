import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Service} from "../model/service";

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

  public addNewService(service: Service): Observable<Service> {
    return this.httpClient.post<Service>(environment.API_BASE_URL + 'api/service', service);
  }
}

