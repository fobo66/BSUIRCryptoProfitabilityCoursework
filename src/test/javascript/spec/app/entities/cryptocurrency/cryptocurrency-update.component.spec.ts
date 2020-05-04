import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { CryptocurrencyUpdateComponent } from 'app/entities/cryptocurrency/cryptocurrency-update.component';
import { CryptocurrencyService } from 'app/entities/cryptocurrency/cryptocurrency.service';
import { Cryptocurrency } from 'app/shared/model/cryptocurrency.model';

describe('Component Tests', () => {
  describe('Cryptocurrency Management Update Component', () => {
    let comp: CryptocurrencyUpdateComponent;
    let fixture: ComponentFixture<CryptocurrencyUpdateComponent>;
    let service: CryptocurrencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [CryptocurrencyUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CryptocurrencyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CryptocurrencyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CryptocurrencyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Cryptocurrency(123);
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
        const entity = new Cryptocurrency();
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
