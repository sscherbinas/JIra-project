import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JiraTestModule } from '../../../test.module';
import { TestCaseComponent } from 'app/entities/test-case/test-case.component';
import { TestCaseService } from 'app/entities/test-case/test-case.service';
import { TestCase } from 'app/shared/model/test-case.model';

describe('Component Tests', () => {
  describe('TestCase Management Component', () => {
    let comp: TestCaseComponent;
    let fixture: ComponentFixture<TestCaseComponent>;
    let service: TestCaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [TestCaseComponent]
      })
        .overrideTemplate(TestCaseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TestCaseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TestCaseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TestCase(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.testCases && comp.testCases[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
