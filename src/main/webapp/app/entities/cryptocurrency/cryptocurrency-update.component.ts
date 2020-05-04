import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICryptocurrency, Cryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { CryptocurrencyService } from './cryptocurrency.service';

@Component({
  selector: 'jhi-cryptocurrency-update',
  templateUrl: './cryptocurrency-update.component.html'
})
export class CryptocurrencyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    shortName: [null, [Validators.maxLength(5)]],
    price: [null, [Validators.min(0)]]
  });

  constructor(protected cryptocurrencyService: CryptocurrencyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cryptocurrency }) => {
      this.updateForm(cryptocurrency);
    });
  }

  updateForm(cryptocurrency: ICryptocurrency): void {
    this.editForm.patchValue({
      id: cryptocurrency.id,
      name: cryptocurrency.name,
      shortName: cryptocurrency.shortName,
      price: cryptocurrency.price
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cryptocurrency = this.createFromForm();
    if (cryptocurrency.id !== undefined) {
      this.subscribeToSaveResponse(this.cryptocurrencyService.update(cryptocurrency));
    } else {
      this.subscribeToSaveResponse(this.cryptocurrencyService.create(cryptocurrency));
    }
  }

  private createFromForm(): ICryptocurrency {
    return {
      ...new Cryptocurrency(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      price: this.editForm.get(['price'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICryptocurrency>>): void {
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
