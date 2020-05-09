import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDefect, Defect } from 'app/shared/model/defect.model';
import { DefectService } from './defect.service';
import { DefectComponent } from './defect.component';
import { DefectDetailComponent } from './defect-detail.component';
import { DefectUpdateComponent } from './defect-update.component';

@Injectable({ providedIn: 'root' })
export class DefectResolve implements Resolve<IDefect> {
  constructor(private service: DefectService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDefect> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((defect: HttpResponse<Defect>) => {
          if (defect.body) {
            return of(defect.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Defect());
  }
}

export const defectRoute: Routes = [
  {
    path: '',
    component: DefectComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Defects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DefectDetailComponent,
    resolve: {
      defect: DefectResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Defects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DefectUpdateComponent,
    resolve: {
      defect: DefectResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Defects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DefectUpdateComponent,
    resolve: {
      defect: DefectResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Defects'
    },
    canActivate: [UserRouteAccessService]
  }
];
