import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';

type EntityResponseType = HttpResponse<ICryptocurrency>;
type EntityArrayResponseType = HttpResponse<ICryptocurrency[]>;

@Injectable({ providedIn: 'root' })
export class CryptocurrencyService {
  public resourceUrl = SERVER_API_URL + 'api/cryptocurrencies';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/cryptocurrencies';

  constructor(protected http: HttpClient) {}

  create(cryptocurrency: ICryptocurrency): Observable<EntityResponseType> {
    return this.http.post<ICryptocurrency>(this.resourceUrl, cryptocurrency, { observe: 'response' });
  }

  update(cryptocurrency: ICryptocurrency): Observable<EntityResponseType> {
    return this.http.put<ICryptocurrency>(this.resourceUrl, cryptocurrency, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICryptocurrency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICryptocurrency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICryptocurrency[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
