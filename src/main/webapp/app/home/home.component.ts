import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import {Cryptocurrency} from '../entities/cryptocurrency/cryptocurrency.model';
import {Videocard} from '../entities/videocard/videocard.model';
import {CryptocurrencyService} from "../entities/cryptocurrency/cryptocurrency.service";
import {VideocardService} from "../entities/videocard/videocard.service";
import {ResponseWrapper} from "../shared/model/response-wrapper.model";

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    cryptocurrencies: Cryptocurrency[] = [];
    videocards: Videocard[] = [];
    account: Account;
    modalRef: NgbModalRef;

    formSubmitted: boolean;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private cryptocurrencyService: CryptocurrencyService,
        private videocardService: VideocardService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.formSubmitted = false;
        this.cryptocurrencyService.query()
            .flatMap((res: ResponseWrapper) => {
                this.cryptocurrencies.push(...res.json);
                return this.videocardService.query();
            })
            .subscribe((res: ResponseWrapper) => {
                this.videocards.push(...res.json)
            });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    calculateProfitability() {
        this.formSubmitted = true;
    }
}
