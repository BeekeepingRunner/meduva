import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
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

export interface WorkSchedule {
  timeFrom: Date,
  timeTo: Date
}

export interface Visit {
  id: number,
  timeFrom: Date,
  timeTo: Date
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

  saveAbsenceHours(workerId: number, absenceHours: TimeRange) {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/set-absence-hours/' + workerId, absenceHours);
  }

  getWeeklyWorkHours(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/get-week-work-hours/' + workerId, weekBoundaries);
  }

  getWeeklyOffWorkHours(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/get-week-off-work-hours/' + workerId, weekBoundaries);
  }

  getWeeklyAbsenceHours(workerId: number, weekBoundaries: WeekBoundaries): Observable<any>  {
    return this.httpClient.post(environment.API_BASE_URL + 'api/worker/get-week-absence-hours/' + workerId, weekBoundaries);
  }

  getWeeklyBookedVisitsAsWorker(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + "api/visit/get-week-booked-visits-as-worker/" + workerId, weekBoundaries );
  }

  getWeeklyBookedVisitsAsClient(workerId: number, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + "api/visit/get-week-booked-visits-as-client/" + workerId, weekBoundaries );
  }

  getWeeklyItemUnavailability(itemId: number, weekBoundaries: WeekBoundaries): Observable<any> {
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

  getWeeklyBookedRoomVisits(roomId: number | undefined, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/visit/get-week-booked-room-visit/' + roomId, weekBoundaries);
  }

  getWeeklyBookedItemVisits(itemId: number | undefined, weekBoundaries: WeekBoundaries): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/visit/get-week-booked-item-visit/' + itemId, weekBoundaries);
  }

  deleteDailyAbsenceHours(workerId: number, absenceDayDate: Date) {
    const httpOptions: any = {
      headers: new HttpHeaders({ 'Content-Type' : 'application/json',
         }),
      body: {day: absenceDayDate}
    };

    return this.httpClient.delete(environment.API_BASE_URL + 'api/worker/delete-daily-absence-hours/' + workerId, httpOptions);
  }

  deleteDailyWorkHours(workerId: number, workDay: Date) {
    const httpOptions: any = {
      headers: new HttpHeaders({ 'Content-Type' : 'application/json',
      }),
      body: {day: workDay}
    };

    return this.httpClient.delete(environment.API_BASE_URL + 'api/worker/delete-daily-work-hours/'+ workerId, httpOptions);
  }

  deleteDailyRoomUnavailability(roomId: number | undefined, unavailabilityDate: Date) {
    const httpOptions: any = {
      headers: new HttpHeaders({ 'Content-Type' : 'application/json',
      }),
      body: {day: unavailabilityDate}
    };

    return this.httpClient.delete(environment.API_BASE_URL + 'api/room/delete-day-unavailability/'+ roomId, httpOptions);
  }


  deleteDailyItemUnavailability(itemId: number | undefined, unavailabilityDate: Date) {
    const httpOptions: any = {
      headers: new HttpHeaders({ 'Content-Type' : 'application/json',
      }),
      body: {day: unavailabilityDate}
    };

    return this.httpClient.delete(environment.API_BASE_URL + 'api/equipment/item/delete-day-unavailability/' + itemId, httpOptions);
  }

}
