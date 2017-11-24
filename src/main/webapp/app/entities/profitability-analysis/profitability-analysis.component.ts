import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {ProfitabilityAnalysis} from './profitability-analysis.model';
import {ProfitabilityAnalysisService} from './profitability-analysis.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-profitability-analysis',
    templateUrl: './profitability-analysis.component.html'
})
export class ProfitabilityAnalysisComponent implements OnInit, OnDestroy {
    profitabilityAnalyses: ProfitabilityAnalysis[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(private profitabilityAnalysisService: ProfitabilityAnalysisService,
                private alertService: JhiAlertService,
                private eventManager: JhiEventManager,
                private activatedRoute: ActivatedRoute,
                private principal: Principal) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.profitabilityAnalysisService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.profitabilityAnalyses = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.profitabilityAnalysisService.getUserAnalysises().subscribe(
            (res: ResponseWrapper) => {
                this.profitabilityAnalyses = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProfitabilityAnalyses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProfitabilityAnalysis) {
        return item.id;
    }

    registerChangeInProfitabilityAnalyses() {
        this.eventSubscriber = this.eventManager.subscribe('profitabilityAnalysisListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
