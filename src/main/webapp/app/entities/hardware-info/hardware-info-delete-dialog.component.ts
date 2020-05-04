import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHardwareInfo } from 'app/shared/model/hardware-info.model';
import { HardwareInfoService } from './hardware-info.service';

@Component({
  templateUrl: './hardware-info-delete-dialog.component.html'
})
export class HardwareInfoDeleteDialogComponent {
  hardwareInfo?: IHardwareInfo;

  constructor(
    protected hardwareInfoService: HardwareInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hardwareInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('hardwareInfoListModification');
      this.activeModal.close();
    });
  }
}
