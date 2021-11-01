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
}
