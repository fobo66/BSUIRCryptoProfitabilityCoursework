import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Videocard } from './videocard.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VideocardService {

    private resourceUrl = SERVER_API_URL + 'api/videocards';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/videocards';

    constructor(private http: Http) { }

    create(videocard: Videocard): Observable<Videocard> {
        const copy = this.convert(videocard);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(videocard: Videocard): Observable<Videocard> {
        const copy = this.convert(videocard);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Videocard> {
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

    private convert(videocard: Videocard): Videocard {
        const copy: Videocard = Object.assign({}, videocard);
        return copy;
    }
}
