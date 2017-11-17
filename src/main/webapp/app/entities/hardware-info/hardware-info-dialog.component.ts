import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {HardwareInfo} from './hardware-info.model';
import {HardwareInfoPopupService} from './hardware-info-popup.service';
import {HardwareInfoService} from './hardware-info.service';
import {Videocard, VideocardService} from '../videocard';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-hardware-info-dialog',
    templateUrl: './hardware-info-dialog.component.html'
})
export class HardwareInfoDialogComponent implements OnInit {

    hardwareInfo: HardwareInfo;
    isSaving: boolean;

    videocards: Videocard[];

    constructor(public activeModal: NgbActiveModal,
                private alertService: JhiAlertService,
                private hardwareInfoService: HardwareInfoService,
                private videocardService: VideocardService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.videocardService
            .query({filter: 'hardwareinfo-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.hardwareInfo.videocard || !this.hardwareInfo.videocard.id) {
                    this.videocards = res.json;
                } else {
                    this.videocardService
                        .find(this.hardwareInfo.videocard.id)
                        .subscribe((subRes: Videocard) => {
                            this.videocards = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.hardwareInfo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.hardwareInfoService.update(this.hardwareInfo));
        } else {
            this.subscribeToSaveResponse(
                this.hardwareInfoService.create(this.hardwareInfo));
        }
    }

    trackVideocardById(index: number, item: Videocard) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<HardwareInfo>) {
        result.subscribe((res: HardwareInfo) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: HardwareInfo) {
        this.eventManager.broadcast({name: 'hardwareInfoListModification', content: 'OK'});
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
    selector: 'jhi-hardware-info-popup',
    template: ''
})
export class HardwareInfoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private hardwareInfoPopupService: HardwareInfoPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.hardwareInfoPopupService
                    .open(HardwareInfoDialogComponent as Component, params['id']);
            } else {
                this.hardwareInfoPopupService
                    .open(HardwareInfoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
