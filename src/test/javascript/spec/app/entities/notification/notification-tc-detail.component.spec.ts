/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { NotificationTcDetailComponent } from '../../../../../../main/webapp/app/entities/notification/notification-tc-detail.component';
import { NotificationTcService } from '../../../../../../main/webapp/app/entities/notification/notification-tc.service';
import { NotificationTc } from '../../../../../../main/webapp/app/entities/notification/notification-tc.model';

describe('Component Tests', () => {

    describe('NotificationTc Management Detail Component', () => {
        let comp: NotificationTcDetailComponent;
        let fixture: ComponentFixture<NotificationTcDetailComponent>;
        let service: NotificationTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [NotificationTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    NotificationTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(NotificationTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NotificationTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new NotificationTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.notification).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
