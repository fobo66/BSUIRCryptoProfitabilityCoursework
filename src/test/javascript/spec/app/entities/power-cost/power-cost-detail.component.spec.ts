import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { PowerCostDetailComponent } from 'app/entities/power-cost/power-cost-detail.component';
import { PowerCost } from 'app/shared/model/power-cost.model';

describe('Component Tests', () => {
  describe('PowerCost Management Detail Component', () => {
    let comp: PowerCostDetailComponent;
    let fixture: ComponentFixture<PowerCostDetailComponent>;
    const route = ({ data: of({ powerCost: new PowerCost(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [PowerCostDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PowerCostDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PowerCostDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load powerCost on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.powerCost).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
