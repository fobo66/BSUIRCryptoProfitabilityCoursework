import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cryptocurrency',
        loadChildren: () => import('./cryptocurrency/cryptocurrency.module').then(m => m.CourseworkCryptocurrencyModule)
      },
      {
        path: 'videocard',
        loadChildren: () => import('./videocard/videocard.module').then(m => m.CourseworkVideocardModule)
      },
      {
        path: 'mining-info',
        loadChildren: () => import('./mining-info/mining-info.module').then(m => m.CourseworkMiningInfoModule)
      },
      {
        path: 'power-cost',
        loadChildren: () => import('./power-cost/power-cost.module').then(m => m.CourseworkPowerCostModule)
      },
      {
        path: 'hardware-info',
        loadChildren: () => import('./hardware-info/hardware-info.module').then(m => m.CourseworkHardwareInfoModule)
      },
      {
        path: 'profitability-analysis',
        loadChildren: () =>
          import('./profitability-analysis/profitability-analysis.module').then(m => m.CourseworkProfitabilityAnalysisModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CourseworkEntityModule {}
