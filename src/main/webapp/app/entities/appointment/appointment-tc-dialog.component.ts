import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AppointmentTc } from './appointment-tc.model';
import { AppointmentTcPopupService } from './appointment-tc-popup.service';
import { AppointmentTcService } from './appointment-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { TeamTc, TeamTcService } from '../team';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-appointment-tc-dialog',
    templateUrl: './appointment-tc-dialog.component.html'
})
export class AppointmentTcDialogComponent implements OnInit {

    appointment: AppointmentTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    teams: TeamTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private appointmentService: AppointmentTcService,
        private customUserService: CustomUserTcService,
        private teamService: TeamTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.teamService.query()
            .subscribe((res: ResponseWrapper) => { this.teams = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.appointment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.appointmentService.update(this.appointment));
        } else {
            this.subscribeToSaveResponse(
                this.appointmentService.create(this.appointment));
        }
    }

    private subscribeToSaveResponse(result: Observable<AppointmentTc>) {
        result.subscribe((res: AppointmentTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AppointmentTc) {
        this.eventManager.broadcast({ name: 'appointmentListModification', content: 'OK'});
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

    trackTeamById(index: number, item: TeamTc) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-appointment-tc-popup',
    template: ''
})
export class AppointmentTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appointmentPopupService: AppointmentTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.appointmentPopupService
                    .open(AppointmentTcDialogComponent as Component, params['id']);
            } else {
                this.appointmentPopupService
                    .open(AppointmentTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
