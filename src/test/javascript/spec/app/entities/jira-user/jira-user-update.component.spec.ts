import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { JiraUserUpdateComponent } from 'app/entities/jira-user/jira-user-update.component';
import { JiraUserService } from 'app/entities/jira-user/jira-user.service';
import { JiraUser } from 'app/shared/model/jira-user.model';

describe('Component Tests', () => {
  describe('JiraUser Management Update Component', () => {
    let comp: JiraUserUpdateComponent;
    let fixture: ComponentFixture<JiraUserUpdateComponent>;
    let service: JiraUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [JiraUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JiraUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JiraUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JiraUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JiraUser(123);
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
        const entity = new JiraUser();
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
