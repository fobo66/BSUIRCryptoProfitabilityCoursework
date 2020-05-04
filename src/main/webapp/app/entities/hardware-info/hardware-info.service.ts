import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHardwareInfo } from 'app/shared/model/hardware-info.model';

type EntityResponseType = HttpResponse<IHardwareInfo>;
type EntityArrayResponseType = HttpResponse<IHardwareInfo[]>;

@Injectable({ providedIn: 'root' })
export class HardwareInfoService {
  public resourceUrl = SERVER_API_URL + 'api/hardware-infos';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/hardware-infos';

  constructor(protected http: HttpClient) {}

  create(hardwareInfo: IHardwareInfo): Observable<EntityResponseType> {
    return this.http.post<IHardwareInfo>(this.resourceUrl, hardwareInfo, { observe: 'response' });
  }

  update(hardwareInfo: IHardwareInfo): Observable<EntityResponseType> {
    return this.http.put<IHardwareInfo>(this.resourceUrl, hardwareInfo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHardwareInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHardwareInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHardwareInfo[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
