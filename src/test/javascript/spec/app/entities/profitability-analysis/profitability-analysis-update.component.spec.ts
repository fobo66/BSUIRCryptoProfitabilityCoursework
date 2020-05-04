import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { ProfitabilityAnalysisUpdateComponent } from 'app/entities/profitability-analysis/profitability-analysis-update.component';
import { ProfitabilityAnalysisService } from 'app/entities/profitability-analysis/profitability-analysis.service';
import { ProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';

describe('Component Tests', () => {
  describe('ProfitabilityAnalysis Management Update Component', () => {
    let comp: ProfitabilityAnalysisUpdateComponent;
    let fixture: ComponentFixture<ProfitabilityAnalysisUpdateComponent>;
    let service: ProfitabilityAnalysisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [ProfitabilityAnalysisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProfitabilityAnalysisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfitabilityAnalysisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfitabilityAnalysisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProfitabilityAnalysis(123);
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
        const entity = new ProfitabilityAnalysis();
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
