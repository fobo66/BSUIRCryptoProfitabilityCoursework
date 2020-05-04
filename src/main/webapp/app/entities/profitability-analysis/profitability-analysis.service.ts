import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';

type EntityResponseType = HttpResponse<IProfitabilityAnalysis>;
type EntityArrayResponseType = HttpResponse<IProfitabilityAnalysis[]>;

@Injectable({ providedIn: 'root' })
export class ProfitabilityAnalysisService {
  public resourceUrl = SERVER_API_URL + 'api/profitability-analyses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/profitability-analyses';

  constructor(protected http: HttpClient) {}

  create(profitabilityAnalysis: IProfitabilityAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profitabilityAnalysis);
    return this.http
      .post<IProfitabilityAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(profitabilityAnalysis: IProfitabilityAnalysis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profitabilityAnalysis);
    return this.http
      .put<IProfitabilityAnalysis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProfitabilityAnalysis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProfitabilityAnalysis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProfitabilityAnalysis[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(profitabilityAnalysis: IProfitabilityAnalysis): IProfitabilityAnalysis {
    const copy: IProfitabilityAnalysis = Object.assign({}, profitabilityAnalysis, {
      date: profitabilityAnalysis.date && profitabilityAnalysis.date.isValid() ? profitabilityAnalysis.date.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((profitabilityAnalysis: IProfitabilityAnalysis) => {
        profitabilityAnalysis.date = profitabilityAnalysis.date ? moment(profitabilityAnalysis.date) : undefined;
      });
    }
    return res;
  }
}
