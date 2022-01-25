import {Service} from "./service";

export interface Room {
  id?: number,
  name: string,
  description: string,
  services?: Service[];
  deleted?: boolean
}
