import {Injectable} from '@angular/core';
import {BaseRequestOptions, Http, Response, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

@Injectable()
export class ProfitabilityService {

    private resourceUrl = SERVER_API_URL + 'api/profitability';

    constructor(private http: Http) {
    }

    createRequestOption = (req?: any): BaseRequestOptions => {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('cryptoCurrencyMiningInfo', req.cryptoCurrencyMiningInfo);
            params.set('hardware', req.hardware);
            params.set('city', req.city);

            options.params = params;
        }
        return options;
    };

    calculateProfitability(formData: any): Observable<boolean> {
        return this.http.get(this.resourceUrl, this.createRequestOption(formData))
            .map((res: Response) => res.json());
    }
}
