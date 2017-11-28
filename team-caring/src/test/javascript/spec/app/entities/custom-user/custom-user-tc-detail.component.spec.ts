/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CustomUserTcDetailComponent } from '../../../../../../main/webapp/app/entities/custom-user/custom-user-tc-detail.component';
import { CustomUserTcService } from '../../../../../../main/webapp/app/entities/custom-user/custom-user-tc.service';
import { CustomUserTc } from '../../../../../../main/webapp/app/entities/custom-user/custom-user-tc.model';

describe('Component Tests', () => {

    describe('CustomUserTc Management Detail Component', () => {
        let comp: CustomUserTcDetailComponent;
        let fixture: ComponentFixture<CustomUserTcDetailComponent>;
        let service: CustomUserTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [CustomUserTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CustomUserTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(CustomUserTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CustomUserTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomUserTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CustomUserTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.customUser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
