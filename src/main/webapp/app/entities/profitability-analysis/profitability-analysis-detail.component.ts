import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';

@Component({
  selector: 'jhi-profitability-analysis-detail',
  templateUrl: './profitability-analysis-detail.component.html'
})
export class ProfitabilityAnalysisDetailComponent implements OnInit {
  profitabilityAnalysis: IProfitabilityAnalysis | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profitabilityAnalysis }) => (this.profitabilityAnalysis = profitabilityAnalysis));
  }

  previousState(): void {
    window.history.back();
  }
}
