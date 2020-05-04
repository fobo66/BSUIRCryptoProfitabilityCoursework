import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cryptocurrency } from './cryptocurrency.model';
import { CryptocurrencyPopupService } from './cryptocurrency-popup.service';
import { CryptocurrencyService } from './cryptocurrency.service';

@Component({
    selector: 'jhi-cryptocurrency-delete-dialog',
    templateUrl: './cryptocurrency-delete-dialog.component.html'
})
export class CryptocurrencyDeleteDialogComponent {

    cryptocurrency: Cryptocurrency;

    constructor(
        private cryptocurrencyService: CryptocurrencyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cryptocurrencyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cryptocurrencyListModification',
                content: 'Deleted an cryptocurrency'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cryptocurrency-delete-popup',
    template: ''
})
export class CryptocurrencyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cryptocurrencyPopupService: CryptocurrencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cryptocurrencyPopupService
                .open(CryptocurrencyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
