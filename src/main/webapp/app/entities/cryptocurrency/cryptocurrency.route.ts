import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {CryptocurrencyComponent} from './cryptocurrency.component';
import {CryptocurrencyDetailComponent} from './cryptocurrency-detail.component';
import {CryptocurrencyPopupComponent} from './cryptocurrency-dialog.component';
import {CryptocurrencyDeletePopupComponent} from './cryptocurrency-delete-dialog.component';

export const cryptocurrencyRoute: Routes = [
    {
        path: 'cryptocurrency',
        component: CryptocurrencyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.cryptocurrency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cryptocurrency/:id',
        component: CryptocurrencyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.cryptocurrency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cryptocurrencyPopupRoute: Routes = [
    {
        path: 'cryptocurrency-new',
        component: CryptocurrencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.cryptocurrency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cryptocurrency/:id/edit',
        component: CryptocurrencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.cryptocurrency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cryptocurrency/:id/delete',
        component: CryptocurrencyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.cryptocurrency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
