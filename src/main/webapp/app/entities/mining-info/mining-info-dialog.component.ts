import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {MiningInfo} from './mining-info.model';
import {MiningInfoPopupService} from './mining-info-popup.service';
import {MiningInfoService} from './mining-info.service';
import {Cryptocurrency, CryptocurrencyService} from '../cryptocurrency';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-mining-info-dialog',
    templateUrl: './mining-info-dialog.component.html'
})
export class MiningInfoDialogComponent implements OnInit {

    miningInfo: MiningInfo;
    isSaving: boolean;

    cryptocurrencies: Cryptocurrency[];

    constructor(public activeModal: NgbActiveModal,
                private alertService: JhiAlertService,
                private miningInfoService: MiningInfoService,
                private cryptocurrencyService: CryptocurrencyService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cryptocurrencyService
            .query({filter: 'mininginfo-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.miningInfo.cryptocurrency || !this.miningInfo.cryptocurrency.id) {
                    this.cryptocurrencies = res.json;
                } else {
                    this.cryptocurrencyService
                        .find(this.miningInfo.cryptocurrency.id)
                        .subscribe((subRes: Cryptocurrency) => {
                            this.cryptocurrencies = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.miningInfo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.miningInfoService.update(this.miningInfo));
        } else {
            this.subscribeToSaveResponse(
                this.miningInfoService.create(this.miningInfo));
        }
    }

    private subscribeToSaveResponse(result: Observable<MiningInfo>) {
        result.subscribe((res: MiningInfo) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    trackCryptocurrencyById(index: number, item: Cryptocurrency) {
        return item.id;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    private onSaveSuccess(result: MiningInfo) {
        this.eventManager.broadcast({name: 'miningInfoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }
}

@Component({
    selector: 'jhi-mining-info-popup',
    template: ''
})
export class MiningInfoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private miningInfoPopupService: MiningInfoPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.miningInfoPopupService
                    .open(MiningInfoDialogComponent as Component, params['id']);
            } else {
                this.miningInfoPopupService
                    .open(MiningInfoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
