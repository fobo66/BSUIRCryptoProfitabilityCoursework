import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {HardwareInfo} from './hardware-info.model';
import {HardwareInfoPopupService} from './hardware-info-popup.service';
import {HardwareInfoService} from './hardware-info.service';

@Component({
    selector: 'jhi-hardware-info-delete-dialog',
    templateUrl: './hardware-info-delete-dialog.component.html'
})
export class HardwareInfoDeleteDialogComponent {

    hardwareInfo: HardwareInfo;

    constructor(private hardwareInfoService: HardwareInfoService,
                public activeModal: NgbActiveModal,
                private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hardwareInfoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'hardwareInfoListModification',
                content: 'Deleted an hardwareInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-hardware-info-delete-popup',
    template: ''
})
export class HardwareInfoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private hardwareInfoPopupService: HardwareInfoPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.hardwareInfoPopupService
                .open(HardwareInfoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
