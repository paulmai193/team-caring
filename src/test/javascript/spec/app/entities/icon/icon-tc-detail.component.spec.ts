/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TeamCaringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { IconTcDetailComponent } from '../../../../../../main/webapp/app/entities/icon/icon-tc-detail.component';
import { IconTcService } from '../../../../../../main/webapp/app/entities/icon/icon-tc.service';
import { IconTc } from '../../../../../../main/webapp/app/entities/icon/icon-tc.model';

describe('Component Tests', () => {

    describe('IconTc Management Detail Component', () => {
        let comp: IconTcDetailComponent;
        let fixture: ComponentFixture<IconTcDetailComponent>;
        let service: IconTcService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TeamCaringTestModule],
                declarations: [IconTcDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    IconTcService,
                    JhiEventManager
                ]
            }).overrideTemplate(IconTcDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IconTcDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IconTcService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new IconTc(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.icon).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
