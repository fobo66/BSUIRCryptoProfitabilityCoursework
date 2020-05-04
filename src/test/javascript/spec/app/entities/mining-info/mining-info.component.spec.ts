import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { MiningInfoComponent } from 'app/entities/mining-info/mining-info.component';
import { MiningInfoService } from 'app/entities/mining-info/mining-info.service';
import { MiningInfo } from 'app/shared/model/mining-info.model';

describe('Component Tests', () => {
  describe('MiningInfo Management Component', () => {
    let comp: MiningInfoComponent;
    let fixture: ComponentFixture<MiningInfoComponent>;
    let service: MiningInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [MiningInfoComponent]
      })
        .overrideTemplate(MiningInfoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MiningInfoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiningInfoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MiningInfo(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.miningInfos && comp.miningInfos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
