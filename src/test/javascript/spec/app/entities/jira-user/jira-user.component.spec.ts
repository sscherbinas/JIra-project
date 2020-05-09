import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JiraTestModule } from '../../../test.module';
import { JiraUserComponent } from 'app/entities/jira-user/jira-user.component';
import { JiraUserService } from 'app/entities/jira-user/jira-user.service';
import { JiraUser } from 'app/shared/model/jira-user.model';

describe('Component Tests', () => {
  describe('JiraUser Management Component', () => {
    let comp: JiraUserComponent;
    let fixture: ComponentFixture<JiraUserComponent>;
    let service: JiraUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [JiraUserComponent]
      })
        .overrideTemplate(JiraUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JiraUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JiraUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new JiraUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.jiraUsers && comp.jiraUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
