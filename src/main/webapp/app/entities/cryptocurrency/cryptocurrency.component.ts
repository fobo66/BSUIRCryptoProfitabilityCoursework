import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { CryptocurrencyService } from './cryptocurrency.service';
import { CryptocurrencyDeleteDialogComponent } from './cryptocurrency-delete-dialog.component';

@Component({
  selector: 'jhi-cryptocurrency',
  templateUrl: './cryptocurrency.component.html'
})
export class CryptocurrencyComponent implements OnInit, OnDestroy {
  cryptocurrencies?: ICryptocurrency[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected cryptocurrencyService: CryptocurrencyService,
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
      this.cryptocurrencyService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<ICryptocurrency[]>) => (this.cryptocurrencies = res.body || []));
      return;
    }

    this.cryptocurrencyService.query().subscribe((res: HttpResponse<ICryptocurrency[]>) => (this.cryptocurrencies = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCryptocurrencies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICryptocurrency): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCryptocurrencies(): void {
    this.eventSubscriber = this.eventManager.subscribe('cryptocurrencyListModification', () => this.loadAll());
  }

  delete(cryptocurrency: ICryptocurrency): void {
    const modalRef = this.modalService.open(CryptocurrencyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cryptocurrency = cryptocurrency;
  }
}
