import {Room} from "./room";
import {Service} from "./service";

export interface EquipmentItem {
  id?: number,
  name: string,
  deleted?: boolean,
  room?: Room
}

export interface EquipmentModel {
  id?: number,
  name: string,
  items: EquipmentItem[],
  services: Service[],
  deleted: boolean
}
