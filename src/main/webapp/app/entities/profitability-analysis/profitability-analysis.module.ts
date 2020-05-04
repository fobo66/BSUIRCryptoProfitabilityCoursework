import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { ProfitabilityAnalysisComponent } from './profitability-analysis.component';
import { ProfitabilityAnalysisDetailComponent } from './profitability-analysis-detail.component';
import { ProfitabilityAnalysisUpdateComponent } from './profitability-analysis-update.component';
import { ProfitabilityAnalysisDeleteDialogComponent } from './profitability-analysis-delete-dialog.component';
import { profitabilityAnalysisRoute } from './profitability-analysis.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(profitabilityAnalysisRoute)],
  declarations: [
    ProfitabilityAnalysisComponent,
    ProfitabilityAnalysisDetailComponent,
    ProfitabilityAnalysisUpdateComponent,
    ProfitabilityAnalysisDeleteDialogComponent
  ],
  entryComponents: [ProfitabilityAnalysisDeleteDialogComponent]
})
export class CourseworkProfitabilityAnalysisModule {}
