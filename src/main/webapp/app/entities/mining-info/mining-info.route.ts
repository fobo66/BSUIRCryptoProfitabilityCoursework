import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MiningInfoComponent } from './mining-info.component';
import { MiningInfoDetailComponent } from './mining-info-detail.component';
import { MiningInfoPopupComponent } from './mining-info-dialog.component';
import { MiningInfoDeletePopupComponent } from './mining-info-delete-dialog.component';

export const miningInfoRoute: Routes = [
    {
        path: 'mining-info',
        component: MiningInfoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.miningInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mining-info/:id',
        component: MiningInfoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.miningInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const miningInfoPopupRoute: Routes = [
    {
        path: 'mining-info-new',
        component: MiningInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.miningInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mining-info/:id/edit',
        component: MiningInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.miningInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mining-info/:id/delete',
        component: MiningInfoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.miningInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
