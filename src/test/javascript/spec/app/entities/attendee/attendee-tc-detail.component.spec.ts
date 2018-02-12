/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AttendeeTcDetailComponent } from '../../../../../../main/webapp/app/entities/attendee/attendee-tc-detail.component';
import { AttendeeTcService } from '../../../../../../main/webapp/app/entities/attendee/attendee-tc.service';
import { AttendeeTc } from '../../../../../../main/webapp/app/entities/attendee/attendee-tc.model';

describe('Component Tests', () => {

    describe('AttendeeTc Management Detail Component', () => {
        let comp: AttendeeTcDetailComponent;
        let fixture: ComponentFixture<AttendeeTcDetailComponent>;
        let service: AttendeeTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [AttendeeTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AttendeeTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(AttendeeTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttendeeTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttendeeTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AttendeeTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.attendee).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
