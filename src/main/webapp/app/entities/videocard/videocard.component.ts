import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVideocard } from 'app/shared/model/videocard.model';
import { VideocardService } from './videocard.service';
import { VideocardDeleteDialogComponent } from './videocard-delete-dialog.component';

@Component({
  selector: 'jhi-videocard',
  templateUrl: './videocard.component.html'
})
export class VideocardComponent implements OnInit, OnDestroy {
  videocards?: IVideocard[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected videocardService: VideocardService,
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
      this.videocardService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IVideocard[]>) => (this.videocards = res.body || []));
      return;
    }

    this.videocardService.query().subscribe((res: HttpResponse<IVideocard[]>) => (this.videocards = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVideocards();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVideocard): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVideocards(): void {
    this.eventSubscriber = this.eventManager.subscribe('videocardListModification', () => this.loadAll());
  }

  delete(videocard: IVideocard): void {
    const modalRef = this.modalService.open(VideocardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.videocard = videocard;
  }
}
