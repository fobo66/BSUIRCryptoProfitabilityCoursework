import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from '../../shared';
import { CourseworkAdminModule } from '../../admin/admin.module';
import {
    ProfitabilityAnalysisService,
    ProfitabilityAnalysisPopupService,
    ProfitabilityAnalysisComponent,
    ProfitabilityAnalysisDetailComponent,
    ProfitabilityAnalysisDialogComponent,
    ProfitabilityAnalysisPopupComponent,
    ProfitabilityAnalysisDeletePopupComponent,
    ProfitabilityAnalysisDeleteDialogComponent,
    profitabilityAnalysisRoute,
    profitabilityAnalysisPopupRoute,
} from './';

const ENTITY_STATES = [
    ...profitabilityAnalysisRoute,
    ...profitabilityAnalysisPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        CourseworkAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProfitabilityAnalysisComponent,
        ProfitabilityAnalysisDetailComponent,
        ProfitabilityAnalysisDialogComponent,
        ProfitabilityAnalysisDeleteDialogComponent,
        ProfitabilityAnalysisPopupComponent,
        ProfitabilityAnalysisDeletePopupComponent,
    ],
    entryComponents: [
        ProfitabilityAnalysisComponent,
        ProfitabilityAnalysisDialogComponent,
        ProfitabilityAnalysisPopupComponent,
        ProfitabilityAnalysisDeleteDialogComponent,
        ProfitabilityAnalysisDeletePopupComponent,
    ],
    providers: [
        ProfitabilityAnalysisService,
        ProfitabilityAnalysisPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkProfitabilityAnalysisModule {}
