import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {Cryptocurrency} from './cryptocurrency.model';
import {CryptocurrencyService} from './cryptocurrency.service';

@Component({
    selector: 'jhi-cryptocurrency-detail',
    templateUrl: './cryptocurrency-detail.component.html'
})
export class CryptocurrencyDetailComponent implements OnInit, OnDestroy {

    cryptocurrency: Cryptocurrency;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private cryptocurrencyService: CryptocurrencyService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCryptocurrencies();
    }

    load(id) {
        this.cryptocurrencyService.find(id).subscribe((cryptocurrency) => {
            this.cryptocurrency = cryptocurrency;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCryptocurrencies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cryptocurrencyListModification',
            (response) => this.load(this.cryptocurrency.id)
        );
    }
}
