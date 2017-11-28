/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TeamTcDetailComponent } from '../../../../../../main/webapp/app/entities/team/team-tc-detail.component';
import { TeamTcService } from '../../../../../../main/webapp/app/entities/team/team-tc.service';
import { TeamTc } from '../../../../../../main/webapp/app/entities/team/team-tc.model';

describe('Component Tests', () => {

    describe('TeamTc Management Detail Component', () => {
        let comp: TeamTcDetailComponent;
        let fixture: ComponentFixture<TeamTcDetailComponent>;
        let service: TeamTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [TeamTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TeamTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(TeamTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TeamTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.team).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
