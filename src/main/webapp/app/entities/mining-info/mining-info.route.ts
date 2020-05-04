import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMiningInfo, MiningInfo } from 'app/shared/model/mining-info.model';
import { MiningInfoService } from './mining-info.service';
import { MiningInfoComponent } from './mining-info.component';
import { MiningInfoDetailComponent } from './mining-info-detail.component';
import { MiningInfoUpdateComponent } from './mining-info-update.component';

@Injectable({ providedIn: 'root' })
export class MiningInfoResolve implements Resolve<IMiningInfo> {
  constructor(private service: MiningInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMiningInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((miningInfo: HttpResponse<MiningInfo>) => {
          if (miningInfo.body) {
            return of(miningInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MiningInfo());
  }
}

export const miningInfoRoute: Routes = [
  {
    path: '',
    component: MiningInfoComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.miningInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MiningInfoDetailComponent,
    resolve: {
      miningInfo: MiningInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.miningInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MiningInfoUpdateComponent,
    resolve: {
      miningInfo: MiningInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.miningInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MiningInfoUpdateComponent,
    resolve: {
      miningInfo: MiningInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.miningInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
