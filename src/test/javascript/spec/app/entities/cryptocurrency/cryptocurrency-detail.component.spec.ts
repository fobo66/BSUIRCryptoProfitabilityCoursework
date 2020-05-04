import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { CryptocurrencyDetailComponent } from 'app/entities/cryptocurrency/cryptocurrency-detail.component';
import { Cryptocurrency } from 'app/shared/model/cryptocurrency.model';

describe('Component Tests', () => {
  describe('Cryptocurrency Management Detail Component', () => {
    let comp: CryptocurrencyDetailComponent;
    let fixture: ComponentFixture<CryptocurrencyDetailComponent>;
    const route = ({ data: of({ cryptocurrency: new Cryptocurrency(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [CryptocurrencyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CryptocurrencyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CryptocurrencyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cryptocurrency on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cryptocurrency).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
