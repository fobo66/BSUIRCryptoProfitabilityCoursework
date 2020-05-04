/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CourseworkTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MiningInfoDetailComponent } from '../../../../../../main/webapp/app/entities/mining-info/mining-info-detail.component';
import { MiningInfoService } from '../../../../../../main/webapp/app/entities/mining-info/mining-info.service';
import { MiningInfo } from '../../../../../../main/webapp/app/entities/mining-info/mining-info.model';

describe('Component Tests', () => {

    describe('MiningInfo Management Detail Component', () => {
        let comp: MiningInfoDetailComponent;
        let fixture: ComponentFixture<MiningInfoDetailComponent>;
        let service: MiningInfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [MiningInfoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MiningInfoService,
                    JhiEventManager
                ]
            }).overrideTemplate(MiningInfoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MiningInfoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MiningInfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MiningInfo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.miningInfo).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
