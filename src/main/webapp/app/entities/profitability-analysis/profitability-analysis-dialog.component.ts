import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ProfitabilityAnalysis} from './profitability-analysis.model';
import {ProfitabilityAnalysisPopupService} from './profitability-analysis-popup.service';
import {ProfitabilityAnalysisService} from './profitability-analysis.service';
import {User, UserService} from '../../shared';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-profitability-analysis-dialog',
    templateUrl: './profitability-analysis-dialog.component.html'
})
export class ProfitabilityAnalysisDialogComponent implements OnInit {

    profitabilityAnalysis: ProfitabilityAnalysis;
    isSaving: boolean;

    users: User[];
    dateDp: any;

    constructor(public activeModal: NgbActiveModal,
                private alertService: JhiAlertService,
                private profitabilityAnalysisService: ProfitabilityAnalysisService,
                private userService: UserService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => {
                this.users = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.profitabilityAnalysis.id !== undefined) {
            this.subscribeToSaveResponse(
                this.profitabilityAnalysisService.update(this.profitabilityAnalysis));
        } else {
            this.subscribeToSaveResponse(
                this.profitabilityAnalysisService.create(this.profitabilityAnalysis));
        }
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<ProfitabilityAnalysis>) {
        result.subscribe((res: ProfitabilityAnalysis) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProfitabilityAnalysis) {
        this.eventManager.broadcast({name: 'profitabilityAnalysisListModification', content: 'OK'});
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
    selector: 'jhi-profitability-analysis-popup',
    template: ''
})
export class ProfitabilityAnalysisPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private profitabilityAnalysisPopupService: ProfitabilityAnalysisPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.profitabilityAnalysisPopupService
                    .open(ProfitabilityAnalysisDialogComponent as Component, params['id']);
            } else {
                this.profitabilityAnalysisPopupService
                    .open(ProfitabilityAnalysisDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
