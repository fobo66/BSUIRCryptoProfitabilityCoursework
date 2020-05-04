import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CourseworkTestModule } from '../../../test.module';
import { CryptocurrencyComponent } from 'app/entities/cryptocurrency/cryptocurrency.component';
import { CryptocurrencyService } from 'app/entities/cryptocurrency/cryptocurrency.service';
import { Cryptocurrency } from 'app/shared/model/cryptocurrency.model';

describe('Component Tests', () => {
  describe('Cryptocurrency Management Component', () => {
    let comp: CryptocurrencyComponent;
    let fixture: ComponentFixture<CryptocurrencyComponent>;
    let service: CryptocurrencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [CryptocurrencyComponent]
      })
        .overrideTemplate(CryptocurrencyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CryptocurrencyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CryptocurrencyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Cryptocurrency(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cryptocurrencies && comp.cryptocurrencies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
