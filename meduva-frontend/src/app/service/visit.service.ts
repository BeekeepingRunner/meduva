import { Injectable } from '@angular/core';

export interface Term {
  startTime: string;
  endTime: string;

  serviceName: string;
  workerName: string;
  roomName: string;
  eqItemName: string;
}

const AVAIL_TERMS_KEY = "AVAILABLE_TERMS";

@Injectable({
  providedIn: 'root'
})
export class VisitService {

  constructor() { }

  saveTerms(terms: Term[]) : void {
    window.sessionStorage.setItem(AVAIL_TERMS_KEY, JSON.stringify(terms));
  }

  getAvailTerms() : Term[] {
    let termsJSON: string | null = window.sessionStorage.getItem(AVAIL_TERMS_KEY);
    if (termsJSON) {
      return JSON.parse(termsJSON);
    } else {
      return [];
    }
  }
}
