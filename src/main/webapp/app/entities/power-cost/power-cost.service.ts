import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPowerCost } from 'app/shared/model/power-cost.model';

type EntityResponseType = HttpResponse<IPowerCost>;
type EntityArrayResponseType = HttpResponse<IPowerCost[]>;

@Injectable({ providedIn: 'root' })
export class PowerCostService {
  public resourceUrl = SERVER_API_URL + 'api/power-costs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/power-costs';

  constructor(protected http: HttpClient) {}

  create(powerCost: IPowerCost): Observable<EntityResponseType> {
    return this.http.post<IPowerCost>(this.resourceUrl, powerCost, { observe: 'response' });
  }

  update(powerCost: IPowerCost): Observable<EntityResponseType> {
    return this.http.put<IPowerCost>(this.resourceUrl, powerCost, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPowerCost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPowerCost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPowerCost[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
