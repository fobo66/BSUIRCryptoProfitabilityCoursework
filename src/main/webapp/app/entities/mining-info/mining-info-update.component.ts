import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IMiningInfo, MiningInfo } from 'app/shared/model/mining-info.model';
import { MiningInfoService } from './mining-info.service';
import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { CryptocurrencyService } from 'app/entities/cryptocurrency/cryptocurrency.service';

@Component({
  selector: 'jhi-mining-info-update',
  templateUrl: './mining-info-update.component.html'
})
export class MiningInfoUpdateComponent implements OnInit {
  isSaving = false;
  cryptocurrencies: ICryptocurrency[] = [];

  editForm = this.fb.group({
    id: [],
    difficulty: [null, [Validators.required, Validators.min(0)]],
    blockReward: [null, [Validators.required, Validators.min(0)]],
    cryptocurrency: [null, Validators.required]
  });

  constructor(
    protected miningInfoService: MiningInfoService,
    protected cryptocurrencyService: CryptocurrencyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ miningInfo }) => {
      this.updateForm(miningInfo);

      this.cryptocurrencyService
        .query({ filter: 'mininginfo-is-null' })
        .pipe(
          map((res: HttpResponse<ICryptocurrency[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICryptocurrency[]) => {
          if (!miningInfo.cryptocurrency || !miningInfo.cryptocurrency.id) {
            this.cryptocurrencies = resBody;
          } else {
            this.cryptocurrencyService
              .find(miningInfo.cryptocurrency.id)
              .pipe(
                map((subRes: HttpResponse<ICryptocurrency>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICryptocurrency[]) => (this.cryptocurrencies = concatRes));
          }
        });
    });
  }

  updateForm(miningInfo: IMiningInfo): void {
    this.editForm.patchValue({
      id: miningInfo.id,
      difficulty: miningInfo.difficulty,
      blockReward: miningInfo.blockReward,
      cryptocurrency: miningInfo.cryptocurrency
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const miningInfo = this.createFromForm();
    if (miningInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.miningInfoService.update(miningInfo));
    } else {
      this.subscribeToSaveResponse(this.miningInfoService.create(miningInfo));
    }
  }

  private createFromForm(): IMiningInfo {
    return {
      ...new MiningInfo(),
      id: this.editForm.get(['id'])!.value,
      difficulty: this.editForm.get(['difficulty'])!.value,
      blockReward: this.editForm.get(['blockReward'])!.value,
      cryptocurrency: this.editForm.get(['cryptocurrency'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMiningInfo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICryptocurrency): any {
    return item.id;
  }
}
