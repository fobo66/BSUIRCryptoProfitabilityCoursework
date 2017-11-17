import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {ProfitabilityAnalysis} from './profitability-analysis.model';
import {ProfitabilityAnalysisService} from './profitability-analysis.service';

@Component({
    selector: 'jhi-profitability-analysis-detail',
    templateUrl: './profitability-analysis-detail.component.html'
})
export class ProfitabilityAnalysisDetailComponent implements OnInit, OnDestroy {

    profitabilityAnalysis: ProfitabilityAnalysis;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private profitabilityAnalysisService: ProfitabilityAnalysisService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProfitabilityAnalyses();
    }

    load(id) {
        this.profitabilityAnalysisService.find(id).subscribe((profitabilityAnalysis) => {
            this.profitabilityAnalysis = profitabilityAnalysis;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProfitabilityAnalyses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'profitabilityAnalysisListModification',
            (response) => this.load(this.profitabilityAnalysis.id)
        );
    }
}
