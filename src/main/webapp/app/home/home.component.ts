import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import {Videocard} from '../entities/videocard/videocard.model';
import {VideocardService} from '../entities/videocard/videocard.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {MiningInfoService} from '../entities/mining-info/mining-info.service';
import {MiningInfo} from '../entities/mining-info/mining-info.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    miningInfoList: MiningInfo[] = [];
    videocards: Videocard[] = [];
    account: Account;
    modalRef: NgbModalRef;

    formSubmitted: boolean;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private miningInfoService: MiningInfoService,
        private videocardService: VideocardService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.formSubmitted = false;
        this.miningInfoService.query()
            .flatMap((res: ResponseWrapper) => {
                this.miningInfoList.push(...res.json);
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
