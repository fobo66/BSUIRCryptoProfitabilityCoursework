import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProfitabilityAnalysis, ProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';
import { ProfitabilityAnalysisComponent } from './profitability-analysis.component';
import { ProfitabilityAnalysisDetailComponent } from './profitability-analysis-detail.component';
import { ProfitabilityAnalysisUpdateComponent } from './profitability-analysis-update.component';

@Injectable({ providedIn: 'root' })
export class ProfitabilityAnalysisResolve implements Resolve<IProfitabilityAnalysis> {
  constructor(private service: ProfitabilityAnalysisService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfitabilityAnalysis> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((profitabilityAnalysis: HttpResponse<ProfitabilityAnalysis>) => {
          if (profitabilityAnalysis.body) {
            return of(profitabilityAnalysis.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProfitabilityAnalysis());
  }
}

export const profitabilityAnalysisRoute: Routes = [
  {
    path: '',
    component: ProfitabilityAnalysisComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProfitabilityAnalysisDetailComponent,
    resolve: {
      profitabilityAnalysis: ProfitabilityAnalysisResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProfitabilityAnalysisUpdateComponent,
    resolve: {
      profitabilityAnalysis: ProfitabilityAnalysisResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProfitabilityAnalysisUpdateComponent,
    resolve: {
      profitabilityAnalysis: ProfitabilityAnalysisResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.profitabilityAnalysis.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
