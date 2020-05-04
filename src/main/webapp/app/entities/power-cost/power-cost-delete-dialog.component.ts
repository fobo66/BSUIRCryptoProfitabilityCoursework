import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPowerCost } from 'app/shared/model/power-cost.model';
import { PowerCostService } from './power-cost.service';

@Component({
  templateUrl: './power-cost-delete-dialog.component.html'
})
export class PowerCostDeleteDialogComponent {
  powerCost?: IPowerCost;

  constructor(protected powerCostService: PowerCostService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.powerCostService.delete(id).subscribe(() => {
      this.eventManager.broadcast('powerCostListModification');
      this.activeModal.close();
    });
  }
}
