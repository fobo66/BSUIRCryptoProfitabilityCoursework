import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {HardwareInfoComponent} from './hardware-info.component';
import {HardwareInfoDetailComponent} from './hardware-info-detail.component';
import {HardwareInfoPopupComponent} from './hardware-info-dialog.component';
import {HardwareInfoDeletePopupComponent} from './hardware-info-delete-dialog.component';

export const hardwareInfoRoute: Routes = [
    {
        path: 'hardware-info',
        component: HardwareInfoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.hardwareInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'hardware-info/:id',
        component: HardwareInfoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.hardwareInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hardwareInfoPopupRoute: Routes = [
    {
        path: 'hardware-info-new',
        component: HardwareInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.hardwareInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hardware-info/:id/edit',
        component: HardwareInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.hardwareInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hardware-info/:id/delete',
        component: HardwareInfoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.hardwareInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
