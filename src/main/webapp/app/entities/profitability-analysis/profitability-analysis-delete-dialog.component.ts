import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProfitabilityAnalysis } from './profitability-analysis.model';
import { ProfitabilityAnalysisPopupService } from './profitability-analysis-popup.service';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';

@Component({
    selector: 'jhi-profitability-analysis-delete-dialog',
    templateUrl: './profitability-analysis-delete-dialog.component.html'
})
export class ProfitabilityAnalysisDeleteDialogComponent {

    profitabilityAnalysis: ProfitabilityAnalysis;

    constructor(
        private profitabilityAnalysisService: ProfitabilityAnalysisService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.profitabilityAnalysisService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'profitabilityAnalysisListModification',
                content: 'Deleted an profitabilityAnalysis'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-profitability-analysis-delete-popup',
    template: ''
})
export class ProfitabilityAnalysisDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private profitabilityAnalysisPopupService: ProfitabilityAnalysisPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.profitabilityAnalysisPopupService
                .open(ProfitabilityAnalysisDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
