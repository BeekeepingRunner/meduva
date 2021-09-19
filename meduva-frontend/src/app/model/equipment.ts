export interface EquipmentItem {
  id?: number,
  name: string,
  deleted: boolean
}

export interface EquipmentModel {
  id?: number,
  name: string,
  items: EquipmentItem[],
  deleted: boolean
}
