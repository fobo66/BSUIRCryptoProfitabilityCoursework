import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {HardwareInfo} from './hardware-info.model';
import {HardwareInfoService} from './hardware-info.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-hardware-info',
    templateUrl: './hardware-info.component.html'
})
export class HardwareInfoComponent implements OnInit, OnDestroy {
    hardwareInfos: HardwareInfo[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(private hardwareInfoService: HardwareInfoService,
                private alertService: JhiAlertService,
                private eventManager: JhiEventManager,
                private activatedRoute: ActivatedRoute,
                private principal: Principal) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.hardwareInfoService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.hardwareInfos = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.hardwareInfoService.query().subscribe(
            (res: ResponseWrapper) => {
                this.hardwareInfos = res.json;
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
        this.registerChangeInHardwareInfos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: HardwareInfo) {
        return item.id;
    }

    registerChangeInHardwareInfos() {
        this.eventSubscriber = this.eventManager.subscribe('hardwareInfoListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
