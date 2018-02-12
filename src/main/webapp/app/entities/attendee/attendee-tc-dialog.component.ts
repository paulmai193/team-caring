import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AttendeeTc } from './attendee-tc.model';
import { AttendeeTcPopupService } from './attendee-tc-popup.service';
import { AttendeeTcService } from './attendee-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { AppointmentTc, AppointmentTcService } from '../appointment';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-attendee-tc-dialog',
    templateUrl: './attendee-tc-dialog.component.html'
})
export class AttendeeTcDialogComponent implements OnInit {

    attendee: AttendeeTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    appointments: AppointmentTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private attendeeService: AttendeeTcService,
        private customUserService: CustomUserTcService,
        private appointmentService: AppointmentTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.appointmentService.query()
            .subscribe((res: ResponseWrapper) => { this.appointments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attendee.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attendeeService.update(this.attendee));
        } else {
            this.subscribeToSaveResponse(
                this.attendeeService.create(this.attendee));
        }
    }

    private subscribeToSaveResponse(result: Observable<AttendeeTc>) {
        result.subscribe((res: AttendeeTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AttendeeTc) {
        this.eventManager.broadcast({ name: 'attendeeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCustomUserById(index: number, item: CustomUserTc) {
        return item.id;
    }

    trackAppointmentById(index: number, item: AppointmentTc) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-attendee-tc-popup',
    template: ''
})
export class AttendeeTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attendeePopupService: AttendeeTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.attendeePopupService
                    .open(AttendeeTcDialogComponent as Component, params['id']);
            } else {
                this.attendeePopupService
                    .open(AttendeeTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
