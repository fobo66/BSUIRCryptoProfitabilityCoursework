import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPowerCost } from 'app/shared/model/power-cost.model';
import { PowerCostService } from './power-cost.service';
import { PowerCostDeleteDialogComponent } from './power-cost-delete-dialog.component';

@Component({
  selector: 'jhi-power-cost',
  templateUrl: './power-cost.component.html'
})
export class PowerCostComponent implements OnInit, OnDestroy {
  powerCosts?: IPowerCost[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected powerCostService: PowerCostService,
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
      this.powerCostService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IPowerCost[]>) => (this.powerCosts = res.body || []));
      return;
    }

    this.powerCostService.query().subscribe((res: HttpResponse<IPowerCost[]>) => (this.powerCosts = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPowerCosts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPowerCost): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPowerCosts(): void {
    this.eventSubscriber = this.eventManager.subscribe('powerCostListModification', () => this.loadAll());
  }

  delete(powerCost: IPowerCost): void {
    const modalRef = this.modalService.open(PowerCostDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.powerCost = powerCost;
  }
}
