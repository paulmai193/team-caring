/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AppointmentTcDetailComponent } from '../../../../../../main/webapp/app/entities/appointment/appointment-tc-detail.component';
import { AppointmentTcService } from '../../../../../../main/webapp/app/entities/appointment/appointment-tc.service';
import { AppointmentTc } from '../../../../../../main/webapp/app/entities/appointment/appointment-tc.model';

describe('Component Tests', () => {

    describe('AppointmentTc Management Detail Component', () => {
        let comp: AppointmentTcDetailComponent;
        let fixture: ComponentFixture<AppointmentTcDetailComponent>;
        let service: AppointmentTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [AppointmentTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AppointmentTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(AppointmentTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppointmentTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppointmentTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AppointmentTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.appointment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
