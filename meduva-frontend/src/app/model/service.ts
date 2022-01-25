export interface Service {
  id?: number,
  name: string,
  description: string,
  durationInMin: number,
  price: number,
  itemless: boolean,
  deleted: boolean
}
