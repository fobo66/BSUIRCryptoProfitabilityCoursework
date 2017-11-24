import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {DatePipe} from '@angular/common';

import {
    AccountService,
    AuthServerProvider,
    CourseworkSharedCommonModule,
    CourseworkSharedLibsModule,
    CSRFService,
    HasAnyAuthorityDirective,
    JhiLoginModalComponent,
    JhiSocialComponent,
    LoginModalService,
    LoginService,
    Principal,
    SocialService,
    StateStorageService,
    UserService
} from './';
import {ProfitabilityService} from './profitability/profitability.service';

@NgModule({
    imports: [
        CourseworkSharedLibsModule,
        CourseworkSharedCommonModule
    ],
    declarations: [
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        SocialService,
        UserService,
        DatePipe,
        ProfitabilityService
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        CourseworkSharedCommonModule,
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class CourseworkSharedModule {}
