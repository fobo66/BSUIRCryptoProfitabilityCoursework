import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IVideocard } from 'app/shared/model/videocard.model';

type EntityResponseType = HttpResponse<IVideocard>;
type EntityArrayResponseType = HttpResponse<IVideocard[]>;

@Injectable({ providedIn: 'root' })
export class VideocardService {
  public resourceUrl = SERVER_API_URL + 'api/videocards';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/videocards';

  constructor(protected http: HttpClient) {}

  create(videocard: IVideocard): Observable<EntityResponseType> {
    return this.http.post<IVideocard>(this.resourceUrl, videocard, { observe: 'response' });
  }

  update(videocard: IVideocard): Observable<EntityResponseType> {
    return this.http.put<IVideocard>(this.resourceUrl, videocard, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVideocard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVideocard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVideocard[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
