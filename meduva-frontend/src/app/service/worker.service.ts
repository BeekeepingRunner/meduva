import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WorkerService {

  constructor(private http: HttpClient) { }

  getAllByPerformedService(serviceId: number) : Observable<User[]> {
    return this.http.get<User[]>(environment.API_BASE_URL + 'api/worker/find-by-service/' + serviceId);
  }

  getAllClients(workerId: number): Observable<any> {
    return this.http.get<any>(environment.API_BASE_URL + 'api/worker/find-clients/' + workerId);
  }

  getAllUnregisteredClients(workerId: number): Observable<any> {
    return this.http.get<any>(environment.API_BASE_URL + 'api/worker/find-unregistered-clients/' + workerId);
  }
}
