/* tslint:disable max-line-length */
import {ComponentFixture, TestBed, async, inject} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils, JhiDataUtils, JhiEventManager} from 'ng-jhipster';
import {CourseworkTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {ProfitabilityAnalysisDetailComponent} from '../../../../../../main/webapp/app/entities/profitability-analysis/profitability-analysis-detail.component';
import {ProfitabilityAnalysisService} from '../../../../../../main/webapp/app/entities/profitability-analysis/profitability-analysis.service';
import {ProfitabilityAnalysis} from '../../../../../../main/webapp/app/entities/profitability-analysis/profitability-analysis.model';

describe('Component Tests', () => {

    describe('ProfitabilityAnalysis Management Detail Component', () => {
        let comp: ProfitabilityAnalysisDetailComponent;
        let fixture: ComponentFixture<ProfitabilityAnalysisDetailComponent>;
        let service: ProfitabilityAnalysisService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [ProfitabilityAnalysisDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProfitabilityAnalysisService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProfitabilityAnalysisDetailComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfitabilityAnalysisDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfitabilityAnalysisService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new ProfitabilityAnalysis(10)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.profitabilityAnalysis).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
