import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../../app.constants';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProfitabilityRequest } from './profitability-request';

@Injectable()
export class ProfitabilityService {
  private resourceUrl = SERVER_API_URL + 'api/profitability';

  constructor(private http: HttpClient) {}

  private createRequestParams = (req?: ProfitabilityRequest): HttpParams => {
    const options: HttpParams = new HttpParams();
    if (req) {
      options
        .append('cryptoCurrencyMiningInfo', req.cryptoCurrencyMiningInfo.toString())
        .append('hardware', req.hardware)
        .append('city', req.city);
    }
    return options;
  };

  calculateProfitability(formData: ProfitabilityRequest): Observable<boolean> {
    return this.http.get<boolean>(this.resourceUrl, {
      params: this.createRequestParams(formData)
    });
  }
}
