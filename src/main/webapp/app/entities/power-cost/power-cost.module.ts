import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from '../../shared';
import {
    PowerCostService,
    PowerCostPopupService,
    PowerCostComponent,
    PowerCostDetailComponent,
    PowerCostDialogComponent,
    PowerCostPopupComponent,
    PowerCostDeletePopupComponent,
    PowerCostDeleteDialogComponent,
    powerCostRoute,
    powerCostPopupRoute,
} from './';

const ENTITY_STATES = [
    ...powerCostRoute,
    ...powerCostPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PowerCostComponent,
        PowerCostDetailComponent,
        PowerCostDialogComponent,
        PowerCostDeleteDialogComponent,
        PowerCostPopupComponent,
        PowerCostDeletePopupComponent,
    ],
    entryComponents: [
        PowerCostComponent,
        PowerCostDialogComponent,
        PowerCostPopupComponent,
        PowerCostDeleteDialogComponent,
        PowerCostDeletePopupComponent,
    ],
    providers: [
        PowerCostService,
        PowerCostPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkPowerCostModule {}
