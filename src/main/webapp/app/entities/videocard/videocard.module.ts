import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { VideocardComponent } from './videocard.component';
import { VideocardDetailComponent } from './videocard-detail.component';
import { VideocardUpdateComponent } from './videocard-update.component';
import { VideocardDeleteDialogComponent } from './videocard-delete-dialog.component';
import { videocardRoute } from './videocard.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(videocardRoute)],
  declarations: [VideocardComponent, VideocardDetailComponent, VideocardUpdateComponent, VideocardDeleteDialogComponent],
  entryComponents: [VideocardDeleteDialogComponent]
})
export class CourseworkVideocardModule {}
