import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JiraTestModule } from '../../../test.module';
import { DefectDetailComponent } from 'app/entities/defect/defect-detail.component';
import { Defect } from 'app/shared/model/defect.model';

describe('Component Tests', () => {
  describe('Defect Management Detail Component', () => {
    let comp: DefectDetailComponent;
    let fixture: ComponentFixture<DefectDetailComponent>;
    const route = ({ data: of({ defect: new Defect(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JiraTestModule],
        declarations: [DefectDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DefectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DefectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load defect on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.defect).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
