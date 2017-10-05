import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {CourseworkSharedModule} from '../../shared';
import {
    CryptocurrencyService,
    CryptocurrencyPopupService,
    CryptocurrencyComponent,
    CryptocurrencyDetailComponent,
    CryptocurrencyDialogComponent,
    CryptocurrencyPopupComponent,
    CryptocurrencyDeletePopupComponent,
    CryptocurrencyDeleteDialogComponent,
    cryptocurrencyRoute,
    cryptocurrencyPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cryptocurrencyRoute,
    ...cryptocurrencyPopupRoute,
];

@NgModule({
    imports: [
        CourseworkSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true})
    ],
    declarations: [
        CryptocurrencyComponent,
        CryptocurrencyDetailComponent,
        CryptocurrencyDialogComponent,
        CryptocurrencyDeleteDialogComponent,
        CryptocurrencyPopupComponent,
        CryptocurrencyDeletePopupComponent,
    ],
    entryComponents: [
        CryptocurrencyComponent,
        CryptocurrencyDialogComponent,
        CryptocurrencyPopupComponent,
        CryptocurrencyDeleteDialogComponent,
        CryptocurrencyDeletePopupComponent,
    ],
    providers: [
        CryptocurrencyService,
        CryptocurrencyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkCryptocurrencyModule {
}
