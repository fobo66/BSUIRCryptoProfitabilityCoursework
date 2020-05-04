import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPowerCost, PowerCost } from 'app/shared/model/power-cost.model';
import { PowerCostService } from './power-cost.service';
import { PowerCostComponent } from './power-cost.component';
import { PowerCostDetailComponent } from './power-cost-detail.component';
import { PowerCostUpdateComponent } from './power-cost-update.component';

@Injectable({ providedIn: 'root' })
export class PowerCostResolve implements Resolve<IPowerCost> {
  constructor(private service: PowerCostService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPowerCost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((powerCost: HttpResponse<PowerCost>) => {
          if (powerCost.body) {
            return of(powerCost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PowerCost());
  }
}

export const powerCostRoute: Routes = [
  {
    path: '',
    component: PowerCostComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.powerCost.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PowerCostDetailComponent,
    resolve: {
      powerCost: PowerCostResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.powerCost.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PowerCostUpdateComponent,
    resolve: {
      powerCost: PowerCostResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.powerCost.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PowerCostUpdateComponent,
    resolve: {
      powerCost: PowerCostResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.powerCost.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
