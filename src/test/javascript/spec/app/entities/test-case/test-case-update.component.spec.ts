import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { TestCaseUpdateComponent } from 'app/entities/test-case/test-case-update.component';
import { TestCaseService } from 'app/entities/test-case/test-case.service';
import { TestCase } from 'app/shared/model/test-case.model';

describe('Component Tests', () => {
  describe('TestCase Management Update Component', () => {
    let comp: TestCaseUpdateComponent;
    let fixture: ComponentFixture<TestCaseUpdateComponent>;
    let service: TestCaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [TestCaseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TestCaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TestCaseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TestCaseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TestCase(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TestCase();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
