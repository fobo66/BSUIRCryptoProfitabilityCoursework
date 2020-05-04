/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CourseworkTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CryptocurrencyDetailComponent } from '../../../../../../main/webapp/app/entities/cryptocurrency/cryptocurrency-detail.component';
import { CryptocurrencyService } from '../../../../../../main/webapp/app/entities/cryptocurrency/cryptocurrency.service';
import { Cryptocurrency } from '../../../../../../main/webapp/app/entities/cryptocurrency/cryptocurrency.model';

describe('Component Tests', () => {

    describe('Cryptocurrency Management Detail Component', () => {
        let comp: CryptocurrencyDetailComponent;
        let fixture: ComponentFixture<CryptocurrencyDetailComponent>;
        let service: CryptocurrencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [CryptocurrencyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CryptocurrencyService,
                    JhiEventManager
                ]
            }).overrideTemplate(CryptocurrencyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CryptocurrencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CryptocurrencyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cryptocurrency(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cryptocurrency).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
