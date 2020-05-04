import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMiningInfo } from 'app/shared/model/mining-info.model';
import { MiningInfoService } from './mining-info.service';
import { MiningInfoDeleteDialogComponent } from './mining-info-delete-dialog.component';

@Component({
  selector: 'jhi-mining-info',
  templateUrl: './mining-info.component.html'
})
export class MiningInfoComponent implements OnInit, OnDestroy {
  miningInfos?: IMiningInfo[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected miningInfoService: MiningInfoService,
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
      this.miningInfoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IMiningInfo[]>) => (this.miningInfos = res.body || []));
      return;
    }

    this.miningInfoService.query().subscribe((res: HttpResponse<IMiningInfo[]>) => (this.miningInfos = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMiningInfos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMiningInfo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMiningInfos(): void {
    this.eventSubscriber = this.eventManager.subscribe('miningInfoListModification', () => this.loadAll());
  }

  delete(miningInfo: IMiningInfo): void {
    const modalRef = this.modalService.open(MiningInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.miningInfo = miningInfo;
  }
}
