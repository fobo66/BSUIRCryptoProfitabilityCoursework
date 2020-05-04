import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { MiningInfoService } from '../entities/mining-info/mining-info.service';
import { HardwareInfoService } from '../entities/hardware-info/hardware-info.service';
import { PowerCostService } from '../entities/power-cost/power-cost.service';
import { ProfitabilityService } from '../shared/profitability/profitability.service';
import { HardwareInfo } from 'app/shared/model/hardware-info.model';
import { PowerCost } from 'app/shared/model/power-cost.model';
import { flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { MiningInfo } from 'app/shared/model/mining-info.model';
import { JhiAlertService } from 'ng-jhipster';
import { ProfitabilityRequest } from 'app/shared/profitability/profitability-request';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;

  miningInfoList: MiningInfo[] = [];
  hardwareInfoList: HardwareInfo[] = [];
  powerCostList: PowerCost[] = [];

  formSubmitted = false;
  formData: ProfitabilityRequest = {
    city: '',
    hardware: '',
    cryptoCurrencyMiningInfo: 0
  };

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private miningInfoService: MiningInfoService,
    private hardwareInfoService: HardwareInfoService,
    private powerCostService: PowerCostService,
    private profitabilityService: ProfitabilityService,
    private alertService: JhiAlertService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.loadDataForProfitabilityCalculation();
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  private loadDataForProfitabilityCalculation(): void {
    this.miningInfoService
      .query()
      .pipe(
        flatMap((res: HttpResponse<MiningInfo[]>) => {
          if (res.body != null) {
            this.miningInfoList.push(...res.body);
          }
          return this.hardwareInfoService.query();
        })
      )
      .pipe(
        flatMap((res: HttpResponse<HardwareInfo[]>) => {
          if (res.body != null) {
            this.hardwareInfoList.push(...res.body);
          }
          return this.powerCostService.query();
        })
      )
      .subscribe((res: HttpResponse<PowerCost[]>) => {
        if (res.body != null) {
          this.powerCostList.push(...res.body);
        }
      });
  }

  calculateProfitability(formData: any): void {
    this.formSubmitted = true;
    this.profitabilityService.calculateProfitability(formData).subscribe((result: boolean) => this.onProfitabilityResult(result));
  }

  onProfitabilityResult(result: boolean): void {
    const message = result ? 'home.profitability.profitable' : 'home.profitability.unprofitable';
    if (result) {
      this.alertService.success(message);
    } else {
      this.alertService.error(message);
    }
  }

  resetForm(): void {
    this.formSubmitted = false;
    this.alertService.clear();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
