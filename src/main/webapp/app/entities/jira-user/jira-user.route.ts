import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJiraUser, JiraUser } from 'app/shared/model/jira-user.model';
import { JiraUserService } from './jira-user.service';
import { JiraUserComponent } from './jira-user.component';
import { JiraUserDetailComponent } from './jira-user-detail.component';
import { JiraUserUpdateComponent } from './jira-user-update.component';

@Injectable({ providedIn: 'root' })
export class JiraUserResolve implements Resolve<IJiraUser> {
  constructor(private service: JiraUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJiraUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jiraUser: HttpResponse<JiraUser>) => {
          if (jiraUser.body) {
            return of(jiraUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JiraUser());
  }
}

export const jiraUserRoute: Routes = [
  {
    path: '',
    component: JiraUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'JiraUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JiraUserDetailComponent,
    resolve: {
      jiraUser: JiraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'JiraUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JiraUserUpdateComponent,
    resolve: {
      jiraUser: JiraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'JiraUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JiraUserUpdateComponent,
    resolve: {
      jiraUser: JiraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'JiraUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
