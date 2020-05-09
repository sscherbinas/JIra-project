import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JiraTestModule } from '../../../test.module';
import { SprintComponent } from 'app/entities/sprint/sprint.component';
import { SprintService } from 'app/entities/sprint/sprint.service';
import { Sprint } from 'app/shared/model/sprint.model';

describe('Component Tests', () => {
  describe('Sprint Management Component', () => {
    let comp: SprintComponent;
    let fixture: ComponentFixture<SprintComponent>;
    let service: SprintService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [SprintComponent]
      })
        .overrideTemplate(SprintComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SprintComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SprintService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Sprint(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sprints && comp.sprints[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
