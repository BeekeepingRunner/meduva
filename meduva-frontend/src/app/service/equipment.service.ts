import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/room";
import {environment} from "../../environments/environment";
import {Service} from "../model/service";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";
import {EquipmentModel} from "../model/equipment-model";

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getAllUndeletedEquipmentModels(): Observable<EquipmentModel[]> {
    return this.httpClient.get<EquipmentModel[]>(environment.API_BASE_URL + 'api/equipment/models/all/undeleted');
  }

  /*
  public getModelById(modelId: number): Observable<EquipmentModel> {
    return this.httpClient.get<Service>(environment.API_BASE_URL + 'rooms/' + roomId).pipe(
      map(room => trimJSON(room, ['_links']))
    );
  }

  public addNewRoom(room: Room): Observable<Room> {
    return this.httpClient.post<Service>(environment.API_BASE_URL + 'api/room', room);
  }

  public deleteById(roomId: number | undefined): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/room/' + roomId);
  }
   */
}
