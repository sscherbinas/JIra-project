import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { TestCaseDetailComponent } from 'app/entities/test-case/test-case-detail.component';
import { TestCase } from 'app/shared/model/test-case.model';

describe('Component Tests', () => {
  describe('TestCase Management Detail Component', () => {
    let comp: TestCaseDetailComponent;
    let fixture: ComponentFixture<TestCaseDetailComponent>;
    const route = ({ data: of({ testCase: new TestCase(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [TestCaseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TestCaseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TestCaseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load testCase on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.testCase).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
