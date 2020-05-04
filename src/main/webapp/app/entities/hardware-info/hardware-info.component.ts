import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHardwareInfo } from 'app/shared/model/hardware-info.model';
import { HardwareInfoService } from './hardware-info.service';
import { HardwareInfoDeleteDialogComponent } from './hardware-info-delete-dialog.component';

@Component({
  selector: 'jhi-hardware-info',
  templateUrl: './hardware-info.component.html'
})
export class HardwareInfoComponent implements OnInit, OnDestroy {
  hardwareInfos?: IHardwareInfo[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected hardwareInfoService: HardwareInfoService,
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
      this.hardwareInfoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHardwareInfo[]>) => (this.hardwareInfos = res.body || []));
      return;
    }

    this.hardwareInfoService.query().subscribe((res: HttpResponse<IHardwareInfo[]>) => (this.hardwareInfos = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHardwareInfos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHardwareInfo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHardwareInfos(): void {
    this.eventSubscriber = this.eventManager.subscribe('hardwareInfoListModification', () => this.loadAll());
  }

  delete(hardwareInfo: IHardwareInfo): void {
    const modalRef = this.modalService.open(HardwareInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hardwareInfo = hardwareInfo;
  }
}
