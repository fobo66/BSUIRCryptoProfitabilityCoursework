import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVideocard, Videocard } from 'app/shared/model/videocard.model';
import { VideocardService } from './videocard.service';

@Component({
  selector: 'jhi-videocard-update',
  templateUrl: './videocard-update.component.html'
})
export class VideocardUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    power: []
  });

  constructor(protected videocardService: VideocardService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videocard }) => {
      this.updateForm(videocard);
    });
  }

  updateForm(videocard: IVideocard): void {
    this.editForm.patchValue({
      id: videocard.id,
      name: videocard.name,
      power: videocard.power
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const videocard = this.createFromForm();
    if (videocard.id !== undefined) {
      this.subscribeToSaveResponse(this.videocardService.update(videocard));
    } else {
      this.subscribeToSaveResponse(this.videocardService.create(videocard));
    }
  }

  private createFromForm(): IVideocard {
    return {
      ...new Videocard(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      power: this.editForm.get(['power'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideocard>>): void {
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
}
