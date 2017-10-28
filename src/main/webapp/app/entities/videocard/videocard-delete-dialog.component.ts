import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Videocard} from './videocard.model';
import {VideocardPopupService} from './videocard-popup.service';
import {VideocardService} from './videocard.service';

@Component({
    selector: 'jhi-videocard-delete-dialog',
    templateUrl: './videocard-delete-dialog.component.html'
})
export class VideocardDeleteDialogComponent {

    videocard: Videocard;

    constructor(private videocardService: VideocardService,
                public activeModal: NgbActiveModal,
                private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.videocardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'videocardListModification',
                content: 'Deleted an videocard'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-videocard-delete-popup',
    template: ''
})
export class VideocardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private videocardPopupService: VideocardPopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.videocardPopupService
                .open(VideocardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
