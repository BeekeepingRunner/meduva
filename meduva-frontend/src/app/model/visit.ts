import {Service} from "./service";
import {User} from "./user";
import {Client} from "./client";
import {Room} from "./room";
import {EquipmentItem} from "./equipment";

export interface Visit {
  id: number,
  description: string,
  timeFrom: Date,
  timeTo: Date,
  service: Service,
  visitStatus: VisitStatus
  userVisits: UserVisit[],
  unregisteredClient: Client,
  room: Room,
  eqItems: EquipmentItem[],
  paid: boolean,
}

export interface UserVisit {
  id: number,
  asClient: boolean,
  user: User,
}

export interface VisitStatus {
  id: number,
  name: string,
}
