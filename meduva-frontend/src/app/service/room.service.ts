import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Service} from "../model/service";
import {environment} from "../../environments/environment";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";
import {Room} from "../model/room";

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

  public addNewRoom(room: Room): Observable<Room> {
    return this.httpClient.post<Service>(environment.API_BASE_URL + 'api/room', room);
  }

  public deleteById(roomId: number | undefined): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/service/' + roomId);
  }
}
