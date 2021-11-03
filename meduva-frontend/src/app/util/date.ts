import {DatePipe, formatDate} from "@angular/common";
import {Injectable} from "@angular/core";
import {locale} from "moment";

@Injectable({
  providedIn: 'root'
})
export class DateUtil {

  constructor(private datePipe: DatePipe) { }

  public parseDate(date: Date, pattern: string): string | null {
    return this.datePipe.transform(date, pattern);
  }
}

export function getCurrentFormattedDate(): string {
  let activeDate = new Date();
  return formatDate(activeDate, 'YYYY-MM-dd HH:mm:ss', locale());
}

export function isMonthSame(date: Date | null, date1: Date): boolean {
  if (date != null) {
    return date.getMonth() == date1.getMonth() && date.getFullYear() == date1.getFullYear();
  } else {
    return false;
  }
}

export function substractMonth(date: Date): Date {

  const month = date.getMonth();
  date.setMonth(date.getMonth() - 1);
  while (date.getMonth() === month) {
    date.setDate(date.getDate() - 1);
  }
  return date;
}

export function addMonth(date: Date): Date {

  const month = date.getMonth();
  date.setMonth(date.getMonth() + 1);
  while (date.getMonth() === month) {
    date.setDate(date.getDate() + 1);
  }
  return date;
}
