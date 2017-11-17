import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {PowerCostComponent} from './power-cost.component';
import {PowerCostDetailComponent} from './power-cost-detail.component';
import {PowerCostPopupComponent} from './power-cost-dialog.component';
import {PowerCostDeletePopupComponent} from './power-cost-delete-dialog.component';

export const powerCostRoute: Routes = [
    {
        path: 'power-cost',
        component: PowerCostComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.powerCost.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'power-cost/:id',
        component: PowerCostDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.powerCost.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const powerCostPopupRoute: Routes = [
    {
        path: 'power-cost-new',
        component: PowerCostPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.powerCost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'power-cost/:id/edit',
        component: PowerCostPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.powerCost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'power-cost/:id/delete',
        component: PowerCostDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.powerCost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
