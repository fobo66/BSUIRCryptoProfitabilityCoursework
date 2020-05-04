import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProfitabilityAnalysisComponent } from './profitability-analysis.component';
import { ProfitabilityAnalysisDetailComponent } from './profitability-analysis-detail.component';
import { ProfitabilityAnalysisPopupComponent } from './profitability-analysis-dialog.component';
import { ProfitabilityAnalysisDeletePopupComponent } from './profitability-analysis-delete-dialog.component';

export const profitabilityAnalysisRoute: Routes = [
    {
        path: 'profitability-analysis',
        component: ProfitabilityAnalysisComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'profitability-analysis/:id',
        component: ProfitabilityAnalysisDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const profitabilityAnalysisPopupRoute: Routes = [
    {
        path: 'profitability-analysis-new',
        component: ProfitabilityAnalysisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profitability-analysis/:id/edit',
        component: ProfitabilityAnalysisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profitability-analysis/:id/delete',
        component: ProfitabilityAnalysisDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
