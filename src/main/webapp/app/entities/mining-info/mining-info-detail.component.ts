import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MiningInfo } from './mining-info.model';
import { MiningInfoService } from './mining-info.service';

@Component({
    selector: 'jhi-mining-info-detail',
    templateUrl: './mining-info-detail.component.html'
})
export class MiningInfoDetailComponent implements OnInit, OnDestroy {

    miningInfo: MiningInfo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private miningInfoService: MiningInfoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMiningInfos();
    }

    load(id) {
        this.miningInfoService.find(id).subscribe((miningInfo) => {
            this.miningInfo = miningInfo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMiningInfos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'miningInfoListModification',
            (response) => this.load(this.miningInfo.id)
        );
    }
}
