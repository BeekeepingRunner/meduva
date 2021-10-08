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
const SELECTED_TERM_KEY = "SELECTED_TERM";

@Injectable({
  providedIn: 'root'
})
export class VisitService {

  constructor() { }

  saveAvailTerms(terms: Term[]) : void {
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

  saveSelectedTerm(term: Term) : void {
    window.sessionStorage.setItem(SELECTED_TERM_KEY, JSON.stringify(term));
  }

  getSelectedTerm() : Term | null {
    let termJSON: string | null = window.sessionStorage.getItem(SELECTED_TERM_KEY);
    if (termJSON) {
      return JSON.parse(termJSON);
    } else {
      return null;
    }
  }
}
