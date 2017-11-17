import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {PowerCost} from './power-cost.model';
import {PowerCostService} from './power-cost.service';

@Component({
    selector: 'jhi-power-cost-detail',
    templateUrl: './power-cost-detail.component.html'
})
export class PowerCostDetailComponent implements OnInit, OnDestroy {

    powerCost: PowerCost;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private powerCostService: PowerCostService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPowerCosts();
    }

    load(id) {
        this.powerCostService.find(id).subscribe((powerCost) => {
            this.powerCost = powerCost;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPowerCosts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'powerCostListModification',
            (response) => this.load(this.powerCost.id)
        );
    }
}
