import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Service} from "../model/service";
import {User} from "../model/user";

export interface Term {
  startTime: string,
  endTime: string,

  serviceId: number,
  workerId: number,
  clientId: number,
  roomId: number,
  eqItemId: number,
}

enum VisitKey {
  AVAIL_TERMS_KEY = "AVAILABLE_TERMS",
  AVAIL_DATES_KEY = "AVAILABLE_DATES",
  SELECTED_TERM_KEY = "SELECTED_TERM",
  SELECTED_SERVICE_KEY = "SELECTED_SERVICE_KEY",
  SELECTED_WORKER_KEY = "SELECTED_WORKER_KEY",
}

@Injectable({
  providedIn: 'root'
})
export class VisitService {

  availableDates: Date[] = [];

  constructor(
    private httpClient: HttpClient,
  ) { }

  saveSelectedService(service: Service) : void {
    window.sessionStorage.setItem(VisitKey.SELECTED_SERVICE_KEY, JSON.stringify(service));
  }

  saveSelectedWorker(worker: User) : void {
    window.sessionStorage.setItem(VisitKey.SELECTED_WORKER_KEY, JSON.stringify(worker));
  }

  saveAvailableDates(dates: Date[]) : void {
    this.availableDates = dates;
  }

  getAvailableDates() : Date[] {
    return this.availableDates;
  }

  saveSelectedTerm(term: Term) : void {
    window.sessionStorage.setItem(VisitKey.SELECTED_TERM_KEY, JSON.stringify(term));
  }

  getSelectedService() : Service | null {
    let serviceJSON: string | null = window.sessionStorage.getItem(VisitKey.SELECTED_SERVICE_KEY);
    if (serviceJSON) {
      return JSON.parse(serviceJSON);
    } else {
      return null;
    }
  }

  getSelectedWorker() : User | null {
    let workerJSON: string | null = window.sessionStorage.getItem(VisitKey.SELECTED_WORKER_KEY);
    if (workerJSON) {
      return JSON.parse(workerJSON);
    } else {
      return null;
    }
  }

  getSelectedTerm() : Term | null {
    let termJSON: string | null = window.sessionStorage.getItem(VisitKey.SELECTED_TERM_KEY);
    if (termJSON) {
      return JSON.parse(termJSON);
    } else {
      return null;
    }
  }

  getWorkerAvailableDaysInMonth(workerID: number, serviceID: number, anyDayFromMonth: string): Observable<any> {
    return this.httpClient.get(environment.API_BASE_URL + 'api/visit/get-worker-available-days-in-month', { params: {
        workerId: workerID,
        serviceId: serviceID,
        anyDayFromMonth: anyDayFromMonth
    }});
  }

  getAvailableDaysInMonth(serviceID: number, anyDayFromMonth: string): Observable<any> {
    return this.httpClient.get(environment.API_BASE_URL + 'api/visit/get-available-days-in-month', { params: {
        serviceId: serviceID,
        anyDayFromMonth: anyDayFromMonth
    }});
  }

  getWorkerTermsForDay(workerId: number, serviceId: number, dayDate: string): Observable<any> {
    return this.httpClient.get(environment.API_BASE_URL + 'api/visit/get-available-worker-terms-for-day',
      {
        params: {
          workerId: workerId,
          serviceId: serviceId,
          dayDate: dayDate
        }});
  }

  saveVisit(term: Term | null): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/visit', term);
  }

  getAllAsClientByUserId(userId: number): Observable<any>  {
    return this.httpClient.get(environment.API_BASE_URL + 'api/visit/all-as-client-by-user-id/' + userId);
  }
}
