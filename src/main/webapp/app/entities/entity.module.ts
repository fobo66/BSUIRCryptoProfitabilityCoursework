import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import {CourseworkCryptocurrencyModule} from './cryptocurrency/cryptocurrency.module';
import {CourseworkVideocardModule} from './videocard/videocard.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CourseworkCryptocurrencyModule,
        CourseworkVideocardModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkEntityModule {}
