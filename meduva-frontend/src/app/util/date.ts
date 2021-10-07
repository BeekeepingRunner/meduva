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
