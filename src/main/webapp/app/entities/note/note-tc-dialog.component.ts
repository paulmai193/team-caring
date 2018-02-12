import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { NoteTc } from './note-tc.model';
import { NoteTcPopupService } from './note-tc-popup.service';
import { NoteTcService } from './note-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { AppointmentTc, AppointmentTcService } from '../appointment';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-note-tc-dialog',
    templateUrl: './note-tc-dialog.component.html'
})
export class NoteTcDialogComponent implements OnInit {

    note: NoteTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    appointments: AppointmentTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private noteService: NoteTcService,
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
        if (this.note.id !== undefined) {
            this.subscribeToSaveResponse(
                this.noteService.update(this.note));
        } else {
            this.subscribeToSaveResponse(
                this.noteService.create(this.note));
        }
    }

    private subscribeToSaveResponse(result: Observable<NoteTc>) {
        result.subscribe((res: NoteTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: NoteTc) {
        this.eventManager.broadcast({ name: 'noteListModification', content: 'OK'});
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
    selector: 'jhi-note-tc-popup',
    template: ''
})
export class NoteTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notePopupService: NoteTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notePopupService
                    .open(NoteTcDialogComponent as Component, params['id']);
            } else {
                this.notePopupService
                    .open(NoteTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
