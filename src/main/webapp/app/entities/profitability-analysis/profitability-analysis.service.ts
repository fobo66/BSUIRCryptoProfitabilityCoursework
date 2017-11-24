import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {ProfitabilityAnalysis} from './profitability-analysis.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class ProfitabilityAnalysisService {

    private resourceUrl = SERVER_API_URL + 'api/profitability-analyses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/profitability-analyses';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(profitabilityAnalysis: ProfitabilityAnalysis): Observable<ProfitabilityAnalysis> {
        const copy = this.convert(profitabilityAnalysis);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(profitabilityAnalysis: ProfitabilityAnalysis): Observable<ProfitabilityAnalysis> {
        const copy = this.convert(profitabilityAnalysis);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ProfitabilityAnalysis> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    getUserAnalysises(): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrl}/user`)
            .map((res: Response) => this.convertResponse(res));
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.date = this.dateUtils
            .convertLocalDateFromServer(entity.date);
    }

    private convert(profitabilityAnalysis: ProfitabilityAnalysis): ProfitabilityAnalysis {
        const copy: ProfitabilityAnalysis = Object.assign({}, profitabilityAnalysis);
        copy.date = this.dateUtils
            .convertLocalDateToServer(profitabilityAnalysis.date);
        return copy;
    }
}
