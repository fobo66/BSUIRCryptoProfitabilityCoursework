import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {HardwareInfo} from './hardware-info.model';
import {HardwareInfoService} from './hardware-info.service';

@Component({
    selector: 'jhi-hardware-info-detail',
    templateUrl: './hardware-info-detail.component.html'
})
export class HardwareInfoDetailComponent implements OnInit, OnDestroy {

    hardwareInfo: HardwareInfo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private hardwareInfoService: HardwareInfoService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHardwareInfos();
    }

    load(id) {
        this.hardwareInfoService.find(id).subscribe((hardwareInfo) => {
            this.hardwareInfo = hardwareInfo;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHardwareInfos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'hardwareInfoListModification',
            (response) => this.load(this.hardwareInfo.id)
        );
    }
}
