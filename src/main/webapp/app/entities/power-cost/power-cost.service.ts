import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { PowerCost } from './power-cost.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PowerCostService {

    private resourceUrl = SERVER_API_URL + 'api/power-costs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/power-costs';

    constructor(private http: Http) { }

    create(powerCost: PowerCost): Observable<PowerCost> {
        const copy = this.convert(powerCost);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(powerCost: PowerCost): Observable<PowerCost> {
        const copy = this.convert(powerCost);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PowerCost> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(powerCost: PowerCost): PowerCost {
        const copy: PowerCost = Object.assign({}, powerCost);
        return copy;
    }
}
