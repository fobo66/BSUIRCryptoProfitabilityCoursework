import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVideocard } from 'app/shared/model/videocard.model';
import { VideocardService } from './videocard.service';

@Component({
  templateUrl: './videocard-delete-dialog.component.html'
})
export class VideocardDeleteDialogComponent {
  videocard?: IVideocard;

  constructor(protected videocardService: VideocardService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.videocardService.delete(id).subscribe(() => {
      this.eventManager.broadcast('videocardListModification');
      this.activeModal.close();
    });
  }
}
