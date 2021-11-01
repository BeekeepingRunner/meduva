import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

export interface TimeRange {
  startTime: Date,
  endTime: Date
}

export interface WorkHours {
  startTime: Date,
  endTime: Date
}

export interface WeekBoundaries {
  firstWeekDay: Date,
  lastWeekDay: Date
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

  saveAbsenceHours(workerId: number, absenceHours: WorkHours) {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/set-absence-hours/' + workerId, absenceHours);
  }

  getWeeklyWorkHours(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/get-week-work-hours/' + workerId, weekBoundaries);
  }

  getWeeklyOffWorkHours(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/get-week-off-work-hours/' + workerId, weekBoundaries);
  }

  getWeeklyItemUnavailability(itemId: number, weekBoundaries: TimeRange): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/equipment/item/get-weekly-unavailability/' + itemId, weekBoundaries);
  }

  setItemDayUnavailability(itemId: number, day: Date): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/equipment/item/set-day-unavailability/' + itemId, day);
  }

  getWeeklyRoomUnavailability(roomId: number, weekBoundaries: TimeRange): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/room/get-weekly-unavailability/' + roomId, weekBoundaries);
  }

  setRoomDayUnavailability(roomId: number, day: Date): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/room/set-day-unavailability/' + roomId, day);
  }
}
