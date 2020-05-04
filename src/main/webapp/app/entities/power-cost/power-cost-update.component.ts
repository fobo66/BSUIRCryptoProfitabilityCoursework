import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPowerCost, PowerCost } from 'app/shared/model/power-cost.model';
import { PowerCostService } from './power-cost.service';

@Component({
  selector: 'jhi-power-cost-update',
  templateUrl: './power-cost-update.component.html'
})
export class PowerCostUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    city: [null, [Validators.required]],
    pricePerKilowatt: [null, [Validators.min(0)]]
  });

  constructor(protected powerCostService: PowerCostService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ powerCost }) => {
      this.updateForm(powerCost);
    });
  }

  updateForm(powerCost: IPowerCost): void {
    this.editForm.patchValue({
      id: powerCost.id,
      city: powerCost.city,
      pricePerKilowatt: powerCost.pricePerKilowatt
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const powerCost = this.createFromForm();
    if (powerCost.id !== undefined) {
      this.subscribeToSaveResponse(this.powerCostService.update(powerCost));
    } else {
      this.subscribeToSaveResponse(this.powerCostService.create(powerCost));
    }
  }

  private createFromForm(): IPowerCost {
    return {
      ...new PowerCost(),
      id: this.editForm.get(['id'])!.value,
      city: this.editForm.get(['city'])!.value,
      pricePerKilowatt: this.editForm.get(['pricePerKilowatt'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPowerCost>>): void {
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
