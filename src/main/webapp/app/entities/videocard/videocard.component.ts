import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {Videocard} from './videocard.model';
import {VideocardService} from './videocard.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-videocard',
    templateUrl: './videocard.component.html'
})
export class VideocardComponent implements OnInit, OnDestroy {
    videocards: Videocard[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(private videocardService: VideocardService,
                private alertService: JhiAlertService,
                private eventManager: JhiEventManager,
                private activatedRoute: ActivatedRoute,
                private principal: Principal) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.videocardService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.videocards = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        this.videocardService.query().subscribe(
            (res: ResponseWrapper) => {
                this.videocards = res.json;
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
        this.registerChangeInVideocards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Videocard) {
        return item.id;
    }

    registerChangeInVideocards() {
        this.eventSubscriber = this.eventManager.subscribe('videocardListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
