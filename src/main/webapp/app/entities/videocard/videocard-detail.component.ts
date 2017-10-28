import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {Videocard} from './videocard.model';
import {VideocardService} from './videocard.service';

@Component({
    selector: 'jhi-videocard-detail',
    templateUrl: './videocard-detail.component.html'
})
export class VideocardDetailComponent implements OnInit, OnDestroy {

    videocard: Videocard;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private videocardService: VideocardService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVideocards();
    }

    load(id) {
        this.videocardService.find(id).subscribe((videocard) => {
            this.videocard = videocard;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVideocards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'videocardListModification',
            (response) => this.load(this.videocard.id)
        );
    }
}
