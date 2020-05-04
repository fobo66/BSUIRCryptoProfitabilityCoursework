import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { MiningInfoComponent } from './mining-info.component';
import { MiningInfoDetailComponent } from './mining-info-detail.component';
import { MiningInfoUpdateComponent } from './mining-info-update.component';
import { MiningInfoDeleteDialogComponent } from './mining-info-delete-dialog.component';
import { miningInfoRoute } from './mining-info.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(miningInfoRoute)],
  declarations: [MiningInfoComponent, MiningInfoDetailComponent, MiningInfoUpdateComponent, MiningInfoDeleteDialogComponent],
  entryComponents: [MiningInfoDeleteDialogComponent]
})
export class CourseworkMiningInfoModule {}
