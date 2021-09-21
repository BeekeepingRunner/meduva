import {Room} from "./room";

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
  deleted: boolean
}
