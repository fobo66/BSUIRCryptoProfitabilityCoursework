import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IHardwareInfo, HardwareInfo } from 'app/shared/model/hardware-info.model';
import { HardwareInfoService } from './hardware-info.service';
import { IVideocard } from 'app/shared/model/videocard.model';
import { VideocardService } from 'app/entities/videocard/videocard.service';

@Component({
  selector: 'jhi-hardware-info-update',
  templateUrl: './hardware-info-update.component.html'
})
export class HardwareInfoUpdateComponent implements OnInit {
  isSaving = false;
  videocards: IVideocard[] = [];

  editForm = this.fb.group({
    id: [],
    hashPower: [null, [Validators.required, Validators.min(0)]],
    price: [null, [Validators.required, Validators.min(0)]],
    videocard: [null, Validators.required]
  });

  constructor(
    protected hardwareInfoService: HardwareInfoService,
    protected videocardService: VideocardService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hardwareInfo }) => {
      this.updateForm(hardwareInfo);

      this.videocardService
        .query({ filter: 'hardwareinfo-is-null' })
        .pipe(
          map((res: HttpResponse<IVideocard[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IVideocard[]) => {
          if (!hardwareInfo.videocard || !hardwareInfo.videocard.id) {
            this.videocards = resBody;
          } else {
            this.videocardService
              .find(hardwareInfo.videocard.id)
              .pipe(
                map((subRes: HttpResponse<IVideocard>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IVideocard[]) => (this.videocards = concatRes));
          }
        });
    });
  }

  updateForm(hardwareInfo: IHardwareInfo): void {
    this.editForm.patchValue({
      id: hardwareInfo.id,
      hashPower: hardwareInfo.hashPower,
      price: hardwareInfo.price,
      videocard: hardwareInfo.videocard
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hardwareInfo = this.createFromForm();
    if (hardwareInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.hardwareInfoService.update(hardwareInfo));
    } else {
      this.subscribeToSaveResponse(this.hardwareInfoService.create(hardwareInfo));
    }
  }

  private createFromForm(): IHardwareInfo {
    return {
      ...new HardwareInfo(),
      id: this.editForm.get(['id'])!.value,
      hashPower: this.editForm.get(['hashPower'])!.value,
      price: this.editForm.get(['price'])!.value,
      videocard: this.editForm.get(['videocard'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHardwareInfo>>): void {
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

  trackById(index: number, item: IVideocard): any {
    return item.id;
  }
}
