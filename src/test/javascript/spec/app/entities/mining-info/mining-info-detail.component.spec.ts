import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourseworkTestModule } from '../../../test.module';
import { MiningInfoDetailComponent } from 'app/entities/mining-info/mining-info-detail.component';
import { MiningInfo } from 'app/shared/model/mining-info.model';

describe('Component Tests', () => {
  describe('MiningInfo Management Detail Component', () => {
    let comp: MiningInfoDetailComponent;
    let fixture: ComponentFixture<MiningInfoDetailComponent>;
    const route = ({ data: of({ miningInfo: new MiningInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CourseworkTestModule],
        declarations: [MiningInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MiningInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MiningInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load miningInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.miningInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
