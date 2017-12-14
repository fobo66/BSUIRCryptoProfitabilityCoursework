import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {MiningInfoService} from '../entities/mining-info/mining-info.service';
import {MiningInfo} from '../entities/mining-info/mining-info.model';
import {HardwareInfoService} from '../entities/hardware-info/hardware-info.service';
import {HardwareInfo} from '../entities/hardware-info/hardware-info.model';
import {PowerCostService} from '../entities/power-cost/power-cost.service';
import {PowerCost} from '../entities/power-cost/power-cost.model';
import {ProfitabilityService} from '../shared/profitability/profitability.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    miningInfoList: MiningInfo[] = [];
    hardwareInfoList: HardwareInfo[] = [];
    powerCostList: PowerCost[] = [];
    account: Account;
    modalRef: NgbModalRef;

    formSubmitted: boolean;
    formData = {};

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private alertService: JhiAlertService,
        private miningInfoService: MiningInfoService,
        private hardwareInfoService: HardwareInfoService,
        private powerCostService: PowerCostService,
        private profitabilityService: ProfitabilityService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.formSubmitted = false;
    }

    private loadDataForProfitabilityCalculation() {
        this.miningInfoService.query()
            .flatMap((res: ResponseWrapper) => {
                this.miningInfoList.push(...res.json);
                return this.hardwareInfoService.query();
            })
            .flatMap((res: ResponseWrapper) => {
                this.hardwareInfoList.push(...res.json);
                return this.powerCostService.query();
            })
            .subscribe((res: ResponseWrapper) => {
                this.powerCostList.push(...res.json);
            });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            })
            .then(() => this.loadDataForProfitabilityCalculation());
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    calculateProfitability(formData: any) {
        this.formSubmitted = true;
        this.profitabilityService.calculateProfitability(formData)
            .subscribe((result) => this.onProfitabilityResult(result));
    }

    onProfitabilityResult(result: boolean) {
        const message = result ? 'home.profitability.profitable' : 'home.profitability.unprofitable';
        if (result) {
            this.alertService.success(message);
        } else {
            this.alertService.error(message);
        }
    }

    resetForm() {
        this.formSubmitted = false;
        this.alertService.clear();
    }
}
