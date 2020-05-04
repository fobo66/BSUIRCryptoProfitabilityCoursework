/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CourseworkTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VideocardDetailComponent } from '../../../../../../main/webapp/app/entities/videocard/videocard-detail.component';
import { VideocardService } from '../../../../../../main/webapp/app/entities/videocard/videocard.service';
import { Videocard } from '../../../../../../main/webapp/app/entities/videocard/videocard.model';

describe('Component Tests', () => {

    describe('Videocard Management Detail Component', () => {
        let comp: VideocardDetailComponent;
        let fixture: ComponentFixture<VideocardDetailComponent>;
        let service: VideocardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CourseworkTestModule],
                declarations: [VideocardDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VideocardService,
                    JhiEventManager
                ]
            }).overrideTemplate(VideocardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VideocardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VideocardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Videocard(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.videocard).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
