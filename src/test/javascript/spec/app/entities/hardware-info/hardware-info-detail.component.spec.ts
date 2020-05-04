import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { HardwareInfoDetailComponent } from 'app/entities/hardware-info/hardware-info-detail.component';
import { HardwareInfo } from 'app/shared/model/hardware-info.model';

describe('Component Tests', () => {
  describe('HardwareInfo Management Detail Component', () => {
    let comp: HardwareInfoDetailComponent;
    let fixture: ComponentFixture<HardwareInfoDetailComponent>;
    const route = ({ data: of({ hardwareInfo: new HardwareInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [HardwareInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HardwareInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HardwareInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load hardwareInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hardwareInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
