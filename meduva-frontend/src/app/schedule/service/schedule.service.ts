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


  saveWorkHours(workHoursToSave: WorkHours) {
    // this.httpClient.post(environment.API_BASE_URL + '/api/???');
  }
}
