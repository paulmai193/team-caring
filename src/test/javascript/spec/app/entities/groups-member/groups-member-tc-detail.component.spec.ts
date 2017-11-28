/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GroupsMemberTcDetailComponent } from '../../../../../../main/webapp/app/entities/groups-member/groups-member-tc-detail.component';
import { GroupsMemberTcService } from '../../../../../../main/webapp/app/entities/groups-member/groups-member-tc.service';
import { GroupsMemberTc } from '../../../../../../main/webapp/app/entities/groups-member/groups-member-tc.model';

describe('Component Tests', () => {

    describe('GroupsMemberTc Management Detail Component', () => {
        let comp: GroupsMemberTcDetailComponent;
        let fixture: ComponentFixture<GroupsMemberTcDetailComponent>;
        let service: GroupsMemberTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [GroupsMemberTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GroupsMemberTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(GroupsMemberTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GroupsMemberTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroupsMemberTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GroupsMemberTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.groupsMember).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
