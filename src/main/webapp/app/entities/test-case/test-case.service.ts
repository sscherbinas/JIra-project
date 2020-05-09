import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITestCase } from 'app/shared/model/test-case.model';

type EntityResponseType = HttpResponse<ITestCase>;
type EntityArrayResponseType = HttpResponse<ITestCase[]>;

@Injectable({ providedIn: 'root' })
export class TestCaseService {
  public resourceUrl = SERVER_API_URL + 'api/test-cases';

  constructor(protected http: HttpClient) {}

  create(testCase: ITestCase): Observable<EntityResponseType> {
    return this.http.post<ITestCase>(this.resourceUrl, testCase, { observe: 'response' });
  }

  update(testCase: ITestCase): Observable<EntityResponseType> {
    return this.http.put<ITestCase>(this.resourceUrl, testCase, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestCase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestCase[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
