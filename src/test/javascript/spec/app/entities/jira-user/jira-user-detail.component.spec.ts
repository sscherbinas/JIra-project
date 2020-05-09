import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { JiraUserDetailComponent } from 'app/entities/jira-user/jira-user-detail.component';
import { JiraUser } from 'app/shared/model/jira-user.model';

describe('Component Tests', () => {
  describe('JiraUser Management Detail Component', () => {
    let comp: JiraUserDetailComponent;
    let fixture: ComponentFixture<JiraUserDetailComponent>;
    const route = ({ data: of({ jiraUser: new JiraUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [JiraUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JiraUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JiraUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jiraUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jiraUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
