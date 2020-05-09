import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDefect } from 'app/shared/model/defect.model';

type EntityResponseType = HttpResponse<IDefect>;
type EntityArrayResponseType = HttpResponse<IDefect[]>;

@Injectable({ providedIn: 'root' })
export class DefectService {
  public resourceUrl = SERVER_API_URL + 'api/defects';

  constructor(protected http: HttpClient) {}

  create(defect: IDefect): Observable<EntityResponseType> {
    return this.http.post<IDefect>(this.resourceUrl, defect, { observe: 'response' });
  }

  update(defect: IDefect): Observable<EntityResponseType> {
    return this.http.put<IDefect>(this.resourceUrl, defect, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDefect>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDefect[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
