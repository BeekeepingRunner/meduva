import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/room";
import {environment} from "../../environments/environment";
import {Service} from "../model/service";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";
import {EquipmentItem, EquipmentModel} from "../model/equipment";
import {NewModelRequest} from "../component/facility-resources/equipment/new-model/new-model.component";

@Injectable({
  providedIn: 'root'
})
export class CreatorService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public deleteAllConfiguration(): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/creator/all');
  }
}
