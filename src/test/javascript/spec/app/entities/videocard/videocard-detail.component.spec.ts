import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { VideocardDetailComponent } from 'app/entities/videocard/videocard-detail.component';
import { Videocard } from 'app/shared/model/videocard.model';

describe('Component Tests', () => {
  describe('Videocard Management Detail Component', () => {
    let comp: VideocardDetailComponent;
    let fixture: ComponentFixture<VideocardDetailComponent>;
    const route = ({ data: of({ videocard: new Videocard(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [VideocardDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VideocardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VideocardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load videocard on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.videocard).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
