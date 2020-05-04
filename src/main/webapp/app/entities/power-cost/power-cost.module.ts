import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { PowerCostComponent } from './power-cost.component';
import { PowerCostDetailComponent } from './power-cost-detail.component';
import { PowerCostUpdateComponent } from './power-cost-update.component';
import { PowerCostDeleteDialogComponent } from './power-cost-delete-dialog.component';
import { powerCostRoute } from './power-cost.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(powerCostRoute)],
  declarations: [PowerCostComponent, PowerCostDetailComponent, PowerCostUpdateComponent, PowerCostDeleteDialogComponent],
  entryComponents: [PowerCostDeleteDialogComponent]
})
export class CourseworkPowerCostModule {
}
