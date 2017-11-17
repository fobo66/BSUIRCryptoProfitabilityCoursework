import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {CourseworkSharedModule} from '../../shared';
import {
    MiningInfoService,
    MiningInfoPopupService,
    MiningInfoComponent,
    MiningInfoDetailComponent,
    MiningInfoDialogComponent,
    MiningInfoPopupComponent,
    MiningInfoDeletePopupComponent,
    MiningInfoDeleteDialogComponent,
    miningInfoRoute,
    miningInfoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...miningInfoRoute,
    ...miningInfoPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true})
    ],
    declarations: [
        MiningInfoComponent,
        MiningInfoDetailComponent,
        MiningInfoDialogComponent,
        MiningInfoDeleteDialogComponent,
        MiningInfoPopupComponent,
        MiningInfoDeletePopupComponent,
    ],
    entryComponents: [
        MiningInfoComponent,
        MiningInfoDialogComponent,
        MiningInfoPopupComponent,
        MiningInfoDeleteDialogComponent,
        MiningInfoDeletePopupComponent,
    ],
    providers: [
        MiningInfoService,
        MiningInfoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkMiningInfoModule {
}
