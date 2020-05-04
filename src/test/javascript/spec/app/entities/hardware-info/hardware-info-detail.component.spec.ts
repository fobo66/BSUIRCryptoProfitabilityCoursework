/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CourseworkTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HardwareInfoDetailComponent } from '../../../../../../main/webapp/app/entities/hardware-info/hardware-info-detail.component';
import { HardwareInfoService } from '../../../../../../main/webapp/app/entities/hardware-info/hardware-info.service';
import { HardwareInfo } from '../../../../../../main/webapp/app/entities/hardware-info/hardware-info.model';

describe('Component Tests', () => {

    describe('HardwareInfo Management Detail Component', () => {
        let comp: HardwareInfoDetailComponent;
        let fixture: ComponentFixture<HardwareInfoDetailComponent>;
        let service: HardwareInfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [HardwareInfoDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HardwareInfoService,
                    JhiEventManager
                ]
            }).overrideTemplate(HardwareInfoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HardwareInfoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareInfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new HardwareInfo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.hardwareInfo).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
