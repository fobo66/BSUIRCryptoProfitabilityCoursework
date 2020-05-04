import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Videocard } from './videocard.model';
import { VideocardPopupService } from './videocard-popup.service';
import { VideocardService } from './videocard.service';

@Component({
    selector: 'jhi-videocard-dialog',
    templateUrl: './videocard-dialog.component.html'
})
export class VideocardDialogComponent implements OnInit {

    videocard: Videocard;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private videocardService: VideocardService,
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
        if (this.videocard.id !== undefined) {
            this.subscribeToSaveResponse(
                this.videocardService.update(this.videocard));
        } else {
            this.subscribeToSaveResponse(
                this.videocardService.create(this.videocard));
        }
    }

    private subscribeToSaveResponse(result: Observable<Videocard>) {
        result.subscribe((res: Videocard) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Videocard) {
        this.eventManager.broadcast({ name: 'videocardListModification', content: 'OK'});
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
    selector: 'jhi-videocard-popup',
    template: ''
})
export class VideocardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private videocardPopupService: VideocardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.videocardPopupService
                    .open(VideocardDialogComponent as Component, params['id']);
            } else {
                this.videocardPopupService
                    .open(VideocardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
