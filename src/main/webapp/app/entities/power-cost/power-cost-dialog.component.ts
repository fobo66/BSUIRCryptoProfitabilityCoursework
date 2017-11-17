import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PowerCost} from './power-cost.model';
import {PowerCostPopupService} from './power-cost-popup.service';
import {PowerCostService} from './power-cost.service';

@Component({
    selector: 'jhi-power-cost-dialog',
    templateUrl: './power-cost-dialog.component.html'
})
export class PowerCostDialogComponent implements OnInit {

    powerCost: PowerCost;
    isSaving: boolean;

    constructor(public activeModal: NgbActiveModal,
                private alertService: JhiAlertService,
                private powerCostService: PowerCostService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.powerCost.id !== undefined) {
            this.subscribeToSaveResponse(
                this.powerCostService.update(this.powerCost));
        } else {
            this.subscribeToSaveResponse(
                this.powerCostService.create(this.powerCost));
        }
    }

    private subscribeToSaveResponse(result: Observable<PowerCost>) {
        result.subscribe((res: PowerCost) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: PowerCost) {
        this.eventManager.broadcast({name: 'powerCostListModification', content: 'OK'});
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
    selector: 'jhi-power-cost-popup',
    template: ''
})
export class PowerCostPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private powerCostPopupService: PowerCostPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.powerCostPopupService
                    .open(PowerCostDialogComponent as Component, params['id']);
            } else {
                this.powerCostPopupService
                    .open(PowerCostDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
