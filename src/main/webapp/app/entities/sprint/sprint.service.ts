import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISprint } from 'app/shared/model/sprint.model';

type EntityResponseType = HttpResponse<ISprint>;
type EntityArrayResponseType = HttpResponse<ISprint[]>;

@Injectable({ providedIn: 'root' })
export class SprintService {
  public resourceUrl = SERVER_API_URL + 'api/sprints';

  constructor(protected http: HttpClient) {}

  create(sprint: ISprint): Observable<EntityResponseType> {
    return this.http.post<ISprint>(this.resourceUrl, sprint, { observe: 'response' });
  }

  update(sprint: ISprint): Observable<EntityResponseType> {
    return this.http.put<ISprint>(this.resourceUrl, sprint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISprint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISprint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
