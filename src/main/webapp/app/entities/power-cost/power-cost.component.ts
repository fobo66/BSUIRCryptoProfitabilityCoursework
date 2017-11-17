import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {PowerCost} from './power-cost.model';
import {PowerCostService} from './power-cost.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-power-cost',
    templateUrl: './power-cost.component.html'
})
export class PowerCostComponent implements OnInit, OnDestroy {
    powerCosts: PowerCost[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(private powerCostService: PowerCostService,
                private alertService: JhiAlertService,
                private eventManager: JhiEventManager,
                private activatedRoute: ActivatedRoute,
                private principal: Principal) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.powerCostService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.powerCosts = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.powerCostService.query().subscribe(
            (res: ResponseWrapper) => {
                this.powerCosts = res.json;
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
        this.registerChangeInPowerCosts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PowerCost) {
        return item.id;
    }

    registerChangeInPowerCosts() {
        this.eventSubscriber = this.eventManager.subscribe('powerCostListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
