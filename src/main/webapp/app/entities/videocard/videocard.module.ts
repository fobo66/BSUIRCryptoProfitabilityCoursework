import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from '../../shared';
import {
    VideocardService,
    VideocardPopupService,
    VideocardComponent,
    VideocardDetailComponent,
    VideocardDialogComponent,
    VideocardPopupComponent,
    VideocardDeletePopupComponent,
    VideocardDeleteDialogComponent,
    videocardRoute,
    videocardPopupRoute,
} from './';

const ENTITY_STATES = [
    ...videocardRoute,
    ...videocardPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VideocardComponent,
        VideocardDetailComponent,
        VideocardDialogComponent,
        VideocardDeleteDialogComponent,
        VideocardPopupComponent,
        VideocardDeletePopupComponent,
    ],
    entryComponents: [
        VideocardComponent,
        VideocardDialogComponent,
        VideocardPopupComponent,
        VideocardDeleteDialogComponent,
        VideocardDeletePopupComponent,
    ],
    providers: [
        VideocardService,
        VideocardPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkVideocardModule {}
