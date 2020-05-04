import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { HardwareInfoUpdateComponent } from 'app/entities/hardware-info/hardware-info-update.component';
import { HardwareInfoService } from 'app/entities/hardware-info/hardware-info.service';
import { HardwareInfo } from 'app/shared/model/hardware-info.model';

describe('Component Tests', () => {
  describe('HardwareInfo Management Update Component', () => {
    let comp: HardwareInfoUpdateComponent;
    let fixture: ComponentFixture<HardwareInfoUpdateComponent>;
    let service: HardwareInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [HardwareInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HardwareInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HardwareInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HardwareInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HardwareInfo(123);
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
        const entity = new HardwareInfo();
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
