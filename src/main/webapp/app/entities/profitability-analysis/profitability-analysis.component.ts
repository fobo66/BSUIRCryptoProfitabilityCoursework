import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';
import { ProfitabilityAnalysisDeleteDialogComponent } from './profitability-analysis-delete-dialog.component';

@Component({
  selector: 'jhi-profitability-analysis',
  templateUrl: './profitability-analysis.component.html'
})
export class ProfitabilityAnalysisComponent implements OnInit, OnDestroy {
  profitabilityAnalyses?: IProfitabilityAnalysis[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected profitabilityAnalysisService: ProfitabilityAnalysisService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.profitabilityAnalysisService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IProfitabilityAnalysis[]>) => (this.profitabilityAnalyses = res.body || []));
      return;
    }

    this.profitabilityAnalysisService
      .query()
      .subscribe((res: HttpResponse<IProfitabilityAnalysis[]>) => (this.profitabilityAnalyses = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProfitabilityAnalyses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProfitabilityAnalysis): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProfitabilityAnalyses(): void {
    this.eventSubscriber = this.eventManager.subscribe('profitabilityAnalysisListModification', () => this.loadAll());
  }

  delete(profitabilityAnalysis: IProfitabilityAnalysis): void {
    const modalRef = this.modalService.open(ProfitabilityAnalysisDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.profitabilityAnalysis = profitabilityAnalysis;
  }
}
