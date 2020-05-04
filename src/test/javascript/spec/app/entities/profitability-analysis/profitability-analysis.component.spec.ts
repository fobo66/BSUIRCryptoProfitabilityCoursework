import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { ProfitabilityAnalysisComponent } from 'app/entities/profitability-analysis/profitability-analysis.component';
import { ProfitabilityAnalysisService } from 'app/entities/profitability-analysis/profitability-analysis.service';
import { ProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';

describe('Component Tests', () => {
  describe('ProfitabilityAnalysis Management Component', () => {
    let comp: ProfitabilityAnalysisComponent;
    let fixture: ComponentFixture<ProfitabilityAnalysisComponent>;
    let service: ProfitabilityAnalysisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [ProfitabilityAnalysisComponent]
      })
        .overrideTemplate(ProfitabilityAnalysisComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfitabilityAnalysisComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfitabilityAnalysisService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProfitabilityAnalysis(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.profitabilityAnalyses && comp.profitabilityAnalyses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
