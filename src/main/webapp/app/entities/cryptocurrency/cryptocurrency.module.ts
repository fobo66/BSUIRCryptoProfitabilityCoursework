import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CourseworkSharedModule } from 'app/shared/shared.module';
import { CryptocurrencyComponent } from './cryptocurrency.component';
import { CryptocurrencyDetailComponent } from './cryptocurrency-detail.component';
import { CryptocurrencyUpdateComponent } from './cryptocurrency-update.component';
import { CryptocurrencyDeleteDialogComponent } from './cryptocurrency-delete-dialog.component';
import { cryptocurrencyRoute } from './cryptocurrency.route';

@NgModule({
  imports: [CourseworkSharedModule, RouterModule.forChild(cryptocurrencyRoute)],
  declarations: [
    CryptocurrencyComponent,
    CryptocurrencyDetailComponent,
    CryptocurrencyUpdateComponent,
    CryptocurrencyDeleteDialogComponent
  ],
  entryComponents: [CryptocurrencyDeleteDialogComponent]
})
export class CourseworkCryptocurrencyModule {
}
