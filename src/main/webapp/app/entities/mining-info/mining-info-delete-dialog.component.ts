import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MiningInfo } from './mining-info.model';
import { MiningInfoPopupService } from './mining-info-popup.service';
import { MiningInfoService } from './mining-info.service';

@Component({
    selector: 'jhi-mining-info-delete-dialog',
    templateUrl: './mining-info-delete-dialog.component.html'
})
export class MiningInfoDeleteDialogComponent {

    miningInfo: MiningInfo;

    constructor(
        private miningInfoService: MiningInfoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.miningInfoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'miningInfoListModification',
                content: 'Deleted an miningInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mining-info-delete-popup',
    template: ''
})
export class MiningInfoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private miningInfoPopupService: MiningInfoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.miningInfoPopupService
                .open(MiningInfoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
