import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import {CourseworkCryptocurrencyModule} from './cryptocurrency/cryptocurrency.module';
import {CourseworkVideocardModule} from './videocard/videocard.module';
import {CourseworkMiningInfoModule} from './mining-info/mining-info.module';
import {CourseworkPowerCostModule} from './power-cost/power-cost.module';
import {CourseworkHardwareInfoModule} from './hardware-info/hardware-info.module';
import {CourseworkProfitabilityAnalysisModule} from './profitability-analysis/profitability-analysis.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CourseworkCryptocurrencyModule,
        CourseworkVideocardModule,
        CourseworkMiningInfoModule,
        CourseworkPowerCostModule,
        CourseworkHardwareInfoModule,
        CourseworkProfitabilityAnalysisModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CourseworkEntityModule {}
