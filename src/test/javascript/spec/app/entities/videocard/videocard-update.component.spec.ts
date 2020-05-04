import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { VideocardUpdateComponent } from 'app/entities/videocard/videocard-update.component';
import { VideocardService } from 'app/entities/videocard/videocard.service';
import { Videocard } from 'app/shared/model/videocard.model';

describe('Component Tests', () => {
  describe('Videocard Management Update Component', () => {
    let comp: VideocardUpdateComponent;
    let fixture: ComponentFixture<VideocardUpdateComponent>;
    let service: VideocardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [VideocardUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VideocardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VideocardUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VideocardService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Videocard(123);
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
        const entity = new Videocard();
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
