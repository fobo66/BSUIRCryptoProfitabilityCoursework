import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { PowerCostComponent } from 'app/entities/power-cost/power-cost.component';
import { PowerCostService } from 'app/entities/power-cost/power-cost.service';
import { PowerCost } from 'app/shared/model/power-cost.model';

describe('Component Tests', () => {
  describe('PowerCost Management Component', () => {
    let comp: PowerCostComponent;
    let fixture: ComponentFixture<PowerCostComponent>;
    let service: PowerCostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [PowerCostComponent]
      })
        .overrideTemplate(PowerCostComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PowerCostComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PowerCostService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PowerCost(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.powerCosts && comp.powerCosts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
