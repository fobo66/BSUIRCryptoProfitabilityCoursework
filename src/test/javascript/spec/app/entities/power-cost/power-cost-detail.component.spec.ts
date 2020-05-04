/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CourseworkTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PowerCostDetailComponent } from '../../../../../../main/webapp/app/entities/power-cost/power-cost-detail.component';
import { PowerCostService } from '../../../../../../main/webapp/app/entities/power-cost/power-cost.service';
import { PowerCost } from '../../../../../../main/webapp/app/entities/power-cost/power-cost.model';

describe('Component Tests', () => {

    describe('PowerCost Management Detail Component', () => {
        let comp: PowerCostDetailComponent;
        let fixture: ComponentFixture<PowerCostDetailComponent>;
        let service: PowerCostService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [PowerCostDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PowerCostService,
                    JhiEventManager
                ]
            }).overrideTemplate(PowerCostDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PowerCostDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PowerCostService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PowerCost(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.powerCost).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
