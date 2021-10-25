import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

export interface WorkHours {
  startTime: Date,
  endTime: Date
}

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(
    private httpClient: HttpClient,
  ) { }


  saveWorkHours(workerId: number, workHours: WorkHours): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/set-work-hours/' + workerId, workHours);
  }
}
