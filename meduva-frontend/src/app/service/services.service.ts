import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Service} from "../model/service";
import {map} from "rxjs/operators";
import {trimJSON} from "../util/json/trim";

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getAllServices(): Observable<Service[]> {
    return this.httpClient.get<Service[]>(environment.API_BASE_URL + 'api/service/all');
  }

  public getAllUndeletedServices(): Observable<Service[]> {
    return this.httpClient.get<Service[]>(environment.API_BASE_URL + 'api/service/all/undeleted');
  }

  getAllItemless(): Observable<Service[]> {
    return this.httpClient.get<Service[]>(environment.API_BASE_URL + 'api/service/all/itemless')
  }

  getAllNonItemless(): Observable<Service[]> {
    return this.httpClient.get<Service[]>(environment.API_BASE_URL + 'api/service/all/nonitemless')
  }

  public getById(serviceId: number): Observable<Service> {
    return this.httpClient.get<Service>(environment.API_BASE_URL + 'services/' + serviceId).pipe(
      map(service => trimJSON(service, ['_links']))
    );
  }

  public addNewService(service: Service): Observable<Service> {
    return this.httpClient.post<Service>(environment.API_BASE_URL + 'api/service', service);
  }

  public deleteById(serviceId: number | undefined): Observable<any> {
    return this.httpClient.delete(environment.API_BASE_URL + 'api/service/' + serviceId);
  }

  getTermsForService(serviceId: number): Observable<any> {
    return this.httpClient.get<any>(environment.API_BASE_URL + 'api/visit/terms-for-service/' + serviceId);
  }
}

