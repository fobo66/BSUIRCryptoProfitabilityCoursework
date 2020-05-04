import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { CryptocurrencyService } from './cryptocurrency.service';

@Component({
  templateUrl: './cryptocurrency-delete-dialog.component.html'
})
export class CryptocurrencyDeleteDialogComponent {
  cryptocurrency?: ICryptocurrency;

  constructor(
    protected cryptocurrencyService: CryptocurrencyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cryptocurrencyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cryptocurrencyListModification');
      this.activeModal.close();
    });
  }
}
