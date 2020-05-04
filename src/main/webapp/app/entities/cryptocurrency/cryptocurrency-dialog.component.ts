import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cryptocurrency } from './cryptocurrency.model';
import { CryptocurrencyPopupService } from './cryptocurrency-popup.service';
import { CryptocurrencyService } from './cryptocurrency.service';

@Component({
    selector: 'jhi-cryptocurrency-dialog',
    templateUrl: './cryptocurrency-dialog.component.html'
})
export class CryptocurrencyDialogComponent implements OnInit {

    cryptocurrency: Cryptocurrency;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private cryptocurrencyService: CryptocurrencyService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cryptocurrency.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cryptocurrencyService.update(this.cryptocurrency));
        } else {
            this.subscribeToSaveResponse(
                this.cryptocurrencyService.create(this.cryptocurrency));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cryptocurrency>) {
        result.subscribe((res: Cryptocurrency) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cryptocurrency) {
        this.eventManager.broadcast({ name: 'cryptocurrencyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-cryptocurrency-popup',
    template: ''
})
export class CryptocurrencyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cryptocurrencyPopupService: CryptocurrencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cryptocurrencyPopupService
                    .open(CryptocurrencyDialogComponent as Component, params['id']);
            } else {
                this.cryptocurrencyPopupService
                    .open(CryptocurrencyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
