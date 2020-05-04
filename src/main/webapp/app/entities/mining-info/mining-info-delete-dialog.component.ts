import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMiningInfo } from 'app/shared/model/mining-info.model';
import { MiningInfoService } from './mining-info.service';

@Component({
  templateUrl: './mining-info-delete-dialog.component.html'
})
export class MiningInfoDeleteDialogComponent {
  miningInfo?: IMiningInfo;

  constructor(
    protected miningInfoService: MiningInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.miningInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('miningInfoListModification');
      this.activeModal.close();
    });
  }
}
