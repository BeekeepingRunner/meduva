import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Service} from "../model/service";
import {environment} from "../../environments/environment";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";
import {Room} from "../model/room";
import {Visit} from "../model/visit";

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getAllRooms(): Observable<Room[]> {
    return this.httpClient.get<Room[]>(environment.API_BASE_URL + 'api/room/all');
  }

  public getAllUndeletedRooms(): Observable<Room[]> {
    return this.httpClient.get<Room[]>(environment.API_BASE_URL + 'api/room/all/undeleted');
  }

  public getById(roomId: number): Observable<Room> {
    return this.httpClient.get<Service>(environment.API_BASE_URL + 'rooms/' + roomId).pipe(
      map(room => trimJSON(room, ['_links']))
    );
  }

  public getRoomServices(roomId: number): Observable<Service[]>{
    return this.httpClient.get<servicesResponse>(environment.API_BASE_URL + 'rooms/'+ roomId + '/services').pipe(
      map(services => trimJSON(services._embedded.services, ['_links']))
    )
  }
  public getRoomVisits(roomId: number): Observable<Visit[]>{
    return this.httpClient.get<Visit[]>(environment.API_BASE_URL + 'api/room/'+ roomId + '/visits');
  }
  public addNewRoom(room: Room): Observable<Room> {
    return this.httpClient.post<Service>(environment.API_BASE_URL + 'api/room', room);
  }

  public deleteById(roomId: number | undefined): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/room/' + roomId);
  }

  editServices(roomId: number, editedServiceList: Service[]): Observable<any> {
    return this.httpClient.post(environment.API_BASE_URL + 'api/room/' + roomId + '/edit-services', editedServiceList);
  }

  public deleteAllRoomsPermanently(): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/room/all');
  }
}

interface servicesResponse {
  _embedded: {
    services: Service[];
  }
}
