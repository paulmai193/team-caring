/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SubjectTcDetailComponent } from '../../../../../../main/webapp/app/entities/subject/subject-tc-detail.component';
import { SubjectTcService } from '../../../../../../main/webapp/app/entities/subject/subject-tc.service';
import { SubjectTc } from '../../../../../../main/webapp/app/entities/subject/subject-tc.model';

describe('Component Tests', () => {

    describe('SubjectTc Management Detail Component', () => {
        let comp: SubjectTcDetailComponent;
        let fixture: ComponentFixture<SubjectTcDetailComponent>;
        let service: SubjectTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [SubjectTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SubjectTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(SubjectTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SubjectTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubjectTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SubjectTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.subject).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
