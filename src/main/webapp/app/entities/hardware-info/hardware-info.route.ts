import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHardwareInfo, HardwareInfo } from 'app/shared/model/hardware-info.model';
import { HardwareInfoService } from './hardware-info.service';
import { HardwareInfoComponent } from './hardware-info.component';
import { HardwareInfoDetailComponent } from './hardware-info-detail.component';
import { HardwareInfoUpdateComponent } from './hardware-info-update.component';

@Injectable({ providedIn: 'root' })
export class HardwareInfoResolve implements Resolve<IHardwareInfo> {
  constructor(private service: HardwareInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHardwareInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((hardwareInfo: HttpResponse<HardwareInfo>) => {
          if (hardwareInfo.body) {
            return of(hardwareInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HardwareInfo());
  }
}

export const hardwareInfoRoute: Routes = [
  {
    path: '',
    component: HardwareInfoComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.hardwareInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HardwareInfoDetailComponent,
    resolve: {
      hardwareInfo: HardwareInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.hardwareInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HardwareInfoUpdateComponent,
    resolve: {
      hardwareInfo: HardwareInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.hardwareInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HardwareInfoUpdateComponent,
    resolve: {
      hardwareInfo: HardwareInfoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.hardwareInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
