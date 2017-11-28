/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GroupsTcDetailComponent } from '../../../../../../main/webapp/app/entities/groups/groups-tc-detail.component';
import { GroupsTcService } from '../../../../../../main/webapp/app/entities/groups/groups-tc.service';
import { GroupsTc } from '../../../../../../main/webapp/app/entities/groups/groups-tc.model';

describe('Component Tests', () => {

    describe('GroupsTc Management Detail Component', () => {
        let comp: GroupsTcDetailComponent;
        let fixture: ComponentFixture<GroupsTcDetailComponent>;
        let service: GroupsTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [GroupsTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GroupsTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(GroupsTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GroupsTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroupsTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GroupsTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.groups).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
