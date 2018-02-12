import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { NotificationTc } from './notification-tc.model';
import { NotificationTcPopupService } from './notification-tc-popup.service';
import { NotificationTcService } from './notification-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-notification-tc-dialog',
    templateUrl: './notification-tc-dialog.component.html'
})
export class NotificationTcDialogComponent implements OnInit {

    notification: NotificationTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private notificationService: NotificationTcService,
        private customUserService: CustomUserTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.notification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.notificationService.update(this.notification));
        } else {
            this.subscribeToSaveResponse(
                this.notificationService.create(this.notification));
        }
    }

    private subscribeToSaveResponse(result: Observable<NotificationTc>) {
        result.subscribe((res: NotificationTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: NotificationTc) {
        this.eventManager.broadcast({ name: 'notificationListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-notification-tc-popup',
    template: ''
})
export class NotificationTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationPopupService: NotificationTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notificationPopupService
                    .open(NotificationTcDialogComponent as Component, params['id']);
            } else {
                this.notificationPopupService
                    .open(NotificationTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
