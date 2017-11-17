import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {MiningInfo} from './mining-info.model';
import {MiningInfoService} from './mining-info.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-mining-info',
    templateUrl: './mining-info.component.html'
})
export class MiningInfoComponent implements OnInit, OnDestroy {
    miningInfos: MiningInfo[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(private miningInfoService: MiningInfoService,
                private alertService: JhiAlertService,
                private eventManager: JhiEventManager,
                private activatedRoute: ActivatedRoute,
                private principal: Principal) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.miningInfoService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.miningInfos = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.miningInfoService.query().subscribe(
            (res: ResponseWrapper) => {
                this.miningInfos = res.json;
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
        this.registerChangeInMiningInfos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: MiningInfo) {
        return item.id;
    }

    registerChangeInMiningInfos() {
        this.eventSubscriber = this.eventManager.subscribe('miningInfoListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
