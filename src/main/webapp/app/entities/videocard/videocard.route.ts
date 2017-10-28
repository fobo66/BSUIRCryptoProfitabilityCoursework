import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {VideocardComponent} from './videocard.component';
import {VideocardDetailComponent} from './videocard-detail.component';
import {VideocardPopupComponent} from './videocard-dialog.component';
import {VideocardDeletePopupComponent} from './videocard-delete-dialog.component';

export const videocardRoute: Routes = [
    {
        path: 'videocard',
        component: VideocardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.videocard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'videocard/:id',
        component: VideocardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.videocard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const videocardPopupRoute: Routes = [
    {
        path: 'videocard-new',
        component: VideocardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.videocard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'videocard/:id/edit',
        component: VideocardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.videocard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'videocard/:id/delete',
        component: VideocardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.videocard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
