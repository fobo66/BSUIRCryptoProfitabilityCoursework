import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { ProfitabilityAnalysisDetailComponent } from 'app/entities/profitability-analysis/profitability-analysis-detail.component';
import { ProfitabilityAnalysis } from 'app/shared/model/profitability-analysis.model';

describe('Component Tests', () => {
  describe('ProfitabilityAnalysis Management Detail Component', () => {
    let comp: ProfitabilityAnalysisDetailComponent;
    let fixture: ComponentFixture<ProfitabilityAnalysisDetailComponent>;
    const route = ({ data: of({ profitabilityAnalysis: new ProfitabilityAnalysis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [ProfitabilityAnalysisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProfitabilityAnalysisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProfitabilityAnalysisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load profitabilityAnalysis on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.profitabilityAnalysis).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
