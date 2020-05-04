import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';

@Component({
  templateUrl: './profitability-analysis-delete-dialog.component.html'
})
export class ProfitabilityAnalysisDeleteDialogComponent {
  profitabilityAnalysis?: IProfitabilityAnalysis;

  constructor(
    protected profitabilityAnalysisService: ProfitabilityAnalysisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profitabilityAnalysisService.delete(id).subscribe(() => {
      this.eventManager.broadcast('profitabilityAnalysisListModification');
      this.activeModal.close();
    });
  }
}
