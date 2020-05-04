import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { MiningInfoUpdateComponent } from 'app/entities/mining-info/mining-info-update.component';
import { MiningInfoService } from 'app/entities/mining-info/mining-info.service';
import { MiningInfo } from 'app/shared/model/mining-info.model';

describe('Component Tests', () => {
  describe('MiningInfo Management Update Component', () => {
    let comp: MiningInfoUpdateComponent;
    let fixture: ComponentFixture<MiningInfoUpdateComponent>;
    let service: MiningInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [MiningInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MiningInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MiningInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiningInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MiningInfo(123);
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
        const entity = new MiningInfo();
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
