import {DatePipe} from "@angular/common";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class DateUtil {

  constructor(private datePipe: DatePipe) { }

  public parseDate(date: Date, pattern: string): string | null {
    return this.datePipe.transform(date, pattern);
  }
}

export function isMonthSame(date: Date | null, date1: Date): boolean {
  if (date != null) {
    return date.getMonth() == date1.getMonth() && date.getFullYear() == date1.getFullYear();
  } else {
    return false;
  }
}
