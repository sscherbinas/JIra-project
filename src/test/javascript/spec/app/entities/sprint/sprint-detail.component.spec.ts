import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { SprintDetailComponent } from 'app/entities/sprint/sprint-detail.component';
import { Sprint } from 'app/shared/model/sprint.model';

describe('Component Tests', () => {
  describe('Sprint Management Detail Component', () => {
    let comp: SprintDetailComponent;
    let fixture: ComponentFixture<SprintDetailComponent>;
    const route = ({ data: of({ sprint: new Sprint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [SprintDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SprintDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SprintDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sprint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sprint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
