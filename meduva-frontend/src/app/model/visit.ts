import {Service} from "./service";

export interface Visit {
  id: number,
  description: string,
  timeFrom: Date,
  timeTo: Date,
  service: Service,
  visitStatus: number
}
