import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/room";
import {environment} from "../../environments/environment";
import {Service} from "../model/service";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";
import {EquipmentItem, EquipmentModel} from "../model/equipment";
import {NewModelRequest} from "../component/equipment/new-model/new-model.component";

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

  public getModelById(modelId: number): Observable<EquipmentModel> {
    return this.httpClient.get<EquipmentModel>(environment.API_BASE_URL + 'api/equipment/model/' + modelId);
  }

  getItemById(eqItemId: number): Observable<EquipmentItem> {
    return this.httpClient.get<EquipmentModel>(environment.API_BASE_URL + 'api/equipment/item/' + eqItemId);
  }

  public deleteModelById(modelId: number | undefined): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/equipment/model/' + modelId);
  }

  saveNewModel(newModelReuqest: NewModelRequest): Observable<EquipmentModel> {
    return this.httpClient.post<EquipmentModel>(environment.API_BASE_URL + 'api/equipment/model/new', newModelReuqest);
  }

  doesModelExistByName(modelName: string): Observable<any> {
    return this.httpClient.get(environment.API_BASE_URL + 'api/equipment/model/doesExistWithName/' + modelName);
  }

  saveModelConnections(newModelReuqest: NewModelRequest): Observable<EquipmentModel> {
    return this.httpClient.post<EquipmentModel>(environment.API_BASE_URL + 'api/equipment/model/connect', newModelReuqest);
  }
}
