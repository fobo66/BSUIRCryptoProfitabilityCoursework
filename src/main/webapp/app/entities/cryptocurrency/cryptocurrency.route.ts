import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICryptocurrency, Cryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { CryptocurrencyService } from './cryptocurrency.service';
import { CryptocurrencyComponent } from './cryptocurrency.component';
import { CryptocurrencyDetailComponent } from './cryptocurrency-detail.component';
import { CryptocurrencyUpdateComponent } from './cryptocurrency-update.component';

@Injectable({ providedIn: 'root' })
export class CryptocurrencyResolve implements Resolve<ICryptocurrency> {
  constructor(private service: CryptocurrencyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICryptocurrency> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cryptocurrency: HttpResponse<Cryptocurrency>) => {
          if (cryptocurrency.body) {
            return of(cryptocurrency.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cryptocurrency());
  }
}

export const cryptocurrencyRoute: Routes = [
  {
    path: '',
    component: CryptocurrencyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.cryptocurrency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CryptocurrencyDetailComponent,
    resolve: {
      cryptocurrency: CryptocurrencyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.cryptocurrency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CryptocurrencyUpdateComponent,
    resolve: {
      cryptocurrency: CryptocurrencyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.cryptocurrency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CryptocurrencyUpdateComponent,
    resolve: {
      cryptocurrency: CryptocurrencyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'courseworkApp.cryptocurrency.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
