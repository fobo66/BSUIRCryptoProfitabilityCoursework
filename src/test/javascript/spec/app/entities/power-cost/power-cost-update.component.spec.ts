import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { PowerCostUpdateComponent } from 'app/entities/power-cost/power-cost-update.component';
import { PowerCostService } from 'app/entities/power-cost/power-cost.service';
import { PowerCost } from 'app/shared/model/power-cost.model';

describe('Component Tests', () => {
  describe('PowerCost Management Update Component', () => {
    let comp: PowerCostUpdateComponent;
    let fixture: ComponentFixture<PowerCostUpdateComponent>;
    let service: PowerCostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [PowerCostUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PowerCostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PowerCostUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PowerCostService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PowerCost(123);
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
        const entity = new PowerCost();
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
