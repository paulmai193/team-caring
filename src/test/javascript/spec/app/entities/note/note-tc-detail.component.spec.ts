/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { NoteTcDetailComponent } from '../../../../../../main/webapp/app/entities/note/note-tc-detail.component';
import { NoteTcService } from '../../../../../../main/webapp/app/entities/note/note-tc.service';
import { NoteTc } from '../../../../../../main/webapp/app/entities/note/note-tc.model';

describe('Component Tests', () => {

    describe('NoteTc Management Detail Component', () => {
        let comp: NoteTcDetailComponent;
        let fixture: ComponentFixture<NoteTcDetailComponent>;
        let service: NoteTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [NoteTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    NoteTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(NoteTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NoteTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NoteTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new NoteTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.note).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
