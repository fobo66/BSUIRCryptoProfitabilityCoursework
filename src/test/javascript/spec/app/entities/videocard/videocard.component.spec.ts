import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { VideocardComponent } from 'app/entities/videocard/videocard.component';
import { VideocardService } from 'app/entities/videocard/videocard.service';
import { Videocard } from 'app/shared/model/videocard.model';

describe('Component Tests', () => {
  describe('Videocard Management Component', () => {
    let comp: VideocardComponent;
    let fixture: ComponentFixture<VideocardComponent>;
    let service: VideocardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [VideocardComponent]
      })
        .overrideTemplate(VideocardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VideocardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VideocardService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Videocard(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.videocards && comp.videocards[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
