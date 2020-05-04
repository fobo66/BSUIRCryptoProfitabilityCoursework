import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { HardwareInfoComponent } from './hardware-info.component';
import { HardwareInfoDetailComponent } from './hardware-info-detail.component';
import { HardwareInfoUpdateComponent } from './hardware-info-update.component';
import { HardwareInfoDeleteDialogComponent } from './hardware-info-delete-dialog.component';
import { hardwareInfoRoute } from './hardware-info.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(hardwareInfoRoute)],
  declarations: [HardwareInfoComponent, HardwareInfoDetailComponent, HardwareInfoUpdateComponent, HardwareInfoDeleteDialogComponent],
  entryComponents: [HardwareInfoDeleteDialogComponent]
})
export class CourseworkHardwareInfoModule {}
