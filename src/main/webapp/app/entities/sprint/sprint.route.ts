import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISprint, Sprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';
import { SprintComponent } from './sprint.component';
import { SprintDetailComponent } from './sprint-detail.component';
import { SprintUpdateComponent } from './sprint-update.component';

@Injectable({ providedIn: 'root' })
export class SprintResolve implements Resolve<ISprint> {
  constructor(private service: SprintService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISprint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sprint: HttpResponse<Sprint>) => {
          if (sprint.body) {
            return of(sprint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sprint());
  }
}

export const sprintRoute: Routes = [
  {
    path: '',
    component: SprintComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sprints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SprintDetailComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sprints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SprintUpdateComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sprints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SprintUpdateComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sprints'
    },
    canActivate: [UserRouteAccessService]
  }
];
