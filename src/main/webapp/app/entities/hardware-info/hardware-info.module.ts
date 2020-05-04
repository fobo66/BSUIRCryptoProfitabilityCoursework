import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from '../../shared';
import {
    HardwareInfoService,
    HardwareInfoPopupService,
    HardwareInfoComponent,
    HardwareInfoDetailComponent,
    HardwareInfoDialogComponent,
    HardwareInfoPopupComponent,
    HardwareInfoDeletePopupComponent,
    HardwareInfoDeleteDialogComponent,
    hardwareInfoRoute,
    hardwareInfoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...hardwareInfoRoute,
    ...hardwareInfoPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HardwareInfoComponent,
        HardwareInfoDetailComponent,
        HardwareInfoDialogComponent,
        HardwareInfoDeleteDialogComponent,
        HardwareInfoPopupComponent,
        HardwareInfoDeletePopupComponent,
    ],
    entryComponents: [
        HardwareInfoComponent,
        HardwareInfoDialogComponent,
        HardwareInfoPopupComponent,
        HardwareInfoDeleteDialogComponent,
        HardwareInfoDeletePopupComponent,
    ],
    providers: [
        HardwareInfoService,
        HardwareInfoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkHardwareInfoModule {}
