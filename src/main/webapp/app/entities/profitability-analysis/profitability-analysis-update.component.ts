import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProfitabilityAnalysis, ProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-profitability-analysis-update',
  templateUrl: './profitability-analysis-update.component.html'
})
export class ProfitabilityAnalysisUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    result: [null, [Validators.required]],
    user: [null, Validators.required]
  });

  constructor(
    protected profitabilityAnalysisService: ProfitabilityAnalysisService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profitabilityAnalysis }) => {
      this.updateForm(profitabilityAnalysis);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(profitabilityAnalysis: IProfitabilityAnalysis): void {
    this.editForm.patchValue({
      id: profitabilityAnalysis.id,
      date: profitabilityAnalysis.date,
      result: profitabilityAnalysis.result,
      user: profitabilityAnalysis.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profitabilityAnalysis = this.createFromForm();
    if (profitabilityAnalysis.id !== undefined) {
      this.subscribeToSaveResponse(this.profitabilityAnalysisService.update(profitabilityAnalysis));
    } else {
      this.subscribeToSaveResponse(this.profitabilityAnalysisService.create(profitabilityAnalysis));
    }
  }

  private createFromForm(): IProfitabilityAnalysis {
    return {
      ...new ProfitabilityAnalysis(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      result: this.editForm.get(['result'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfitabilityAnalysis>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
