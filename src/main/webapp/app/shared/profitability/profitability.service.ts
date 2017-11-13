import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

@Injectable()
export class ProfitabilityService {

    private resourceUrl = SERVER_API_URL + 'api/profitability';

    constructor(private http: Http) {
    }

    calculateProfitability(): Observable<boolean> {
        return this.http.get(this.resourceUrl)
            .map((res: Response) => res.json());
    }
}
