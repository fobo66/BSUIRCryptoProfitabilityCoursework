import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { HardwareInfoComponent } from 'app/entities/hardware-info/hardware-info.component';
import { HardwareInfoService } from 'app/entities/hardware-info/hardware-info.service';
import { HardwareInfo } from 'app/shared/model/hardware-info.model';

describe('Component Tests', () => {
  describe('HardwareInfo Management Component', () => {
    let comp: HardwareInfoComponent;
    let fixture: ComponentFixture<HardwareInfoComponent>;
    let service: HardwareInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [HardwareInfoComponent]
      })
        .overrideTemplate(HardwareInfoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HardwareInfoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HardwareInfoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HardwareInfo(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.hardwareInfos && comp.hardwareInfos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
