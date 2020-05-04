import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVideocard, Videocard } from 'app/shared/model/videocard.model';
import { VideocardService } from './videocard.service';
import { VideocardComponent } from './videocard.component';
import { VideocardDetailComponent } from './videocard-detail.component';
import { VideocardUpdateComponent } from './videocard-update.component';

@Injectable({ providedIn: 'root' })
export class VideocardResolve implements Resolve<IVideocard> {
  constructor(private service: VideocardService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVideocard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((videocard: HttpResponse<Videocard>) => {
          if (videocard.body) {
            return of(videocard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Videocard());
  }
}

export const videocardRoute: Routes = [
  {
    path: '',
    component: VideocardComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.videocard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VideocardDetailComponent,
    resolve: {
      videocard: VideocardResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.videocard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VideocardUpdateComponent,
    resolve: {
      videocard: VideocardResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.videocard.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VideocardUpdateComponent,
    resolve: {
      videocard: VideocardResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.videocard.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
