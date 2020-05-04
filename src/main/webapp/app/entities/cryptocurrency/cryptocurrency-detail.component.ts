import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';

@Component({
  selector: 'jhi-cryptocurrency-detail',
  templateUrl: './cryptocurrency-detail.component.html'
})
export class CryptocurrencyDetailComponent implements OnInit {
  cryptocurrency: ICryptocurrency | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cryptocurrency }) => (this.cryptocurrency = cryptocurrency));
  }

  previousState(): void {
    window.history.back();
  }
}
