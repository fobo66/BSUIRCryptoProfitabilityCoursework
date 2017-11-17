import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {PowerCost} from './power-cost.model';
import {PowerCostPopupService} from './power-cost-popup.service';
import {PowerCostService} from './power-cost.service';

@Component({
    selector: 'jhi-power-cost-delete-dialog',
    templateUrl: './power-cost-delete-dialog.component.html'
})
export class PowerCostDeleteDialogComponent {

    powerCost: PowerCost;

    constructor(private powerCostService: PowerCostService,
                public activeModal: NgbActiveModal,
                private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.powerCostService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'powerCostListModification',
                content: 'Deleted an powerCost'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-power-cost-delete-popup',
    template: ''
})
export class PowerCostDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private powerCostPopupService: PowerCostPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.powerCostPopupService
                .open(PowerCostDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
