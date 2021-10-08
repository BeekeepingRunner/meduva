import { Injectable } from '@angular/core';
import {Client} from "../model/client";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

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
  SELECTED_CLIENT_KEY = "SELECTED_CLIENT"
}

@Injectable({
  providedIn: 'root'
})
export class VisitService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  saveAvailTerms(terms: Term[]) : void {
    window.sessionStorage.setItem(VisitKey.AVAIL_TERMS_KEY, JSON.stringify(terms));
  }

  saveSelectedTerm(term: Term) : void {
    window.sessionStorage.setItem(VisitKey.SELECTED_TERM_KEY, JSON.stringify(term));
  }

  getAvailTerms() : Term[] {
    let termsJSON: string | null = window.sessionStorage.getItem(VisitKey.AVAIL_TERMS_KEY);
    if (termsJSON) {
      return JSON.parse(termsJSON);
    } else {
      return [];
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

  saveVisit(term: Term | null): any {
    this.httpClient.post(environment.API_BASE_URL + 'api/visit', term);
  }
}
