import { Injectable } from '@angular/core';
import {Client} from "../model/client";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Service} from "../model/service";

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
  SELECTED_TERM_KEY = "SELECTED_TERM",
  SELECTED_SERVICE_KEY = "SELECTED_SERVICE_KEY",
}

@Injectable({
  providedIn: 'root'
})
export class VisitService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  saveSelectedService(service: Service) : void {
    window.sessionStorage.setItem(VisitKey.SELECTED_SERVICE_KEY, JSON.stringify(service));
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

  getSelectedTerm() : Term | null {
    let termJSON: string | null = window.sessionStorage.getItem(VisitKey.SELECTED_TERM_KEY);
    if (termJSON) {
      return JSON.parse(termJSON);
    } else {
      return null;
    }
  }

  saveVisit(term: Term | null): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/visit', term);
  }
}
