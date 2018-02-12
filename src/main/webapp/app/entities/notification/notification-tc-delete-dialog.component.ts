import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationTc } from './notification-tc.model';
import { NotificationTcPopupService } from './notification-tc-popup.service';
import { NotificationTcService } from './notification-tc.service';

@Component({
    selector: 'jhi-notification-tc-delete-dialog',
    templateUrl: './notification-tc-delete-dialog.component.html'
})
export class NotificationTcDeleteDialogComponent {

    notification: NotificationTc;

    constructor(
        private notificationService: NotificationTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.notificationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'notificationListModification',
                content: 'Deleted an notification'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-notification-tc-delete-popup',
    template: ''
})
export class NotificationTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationPopupService: NotificationTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.notificationPopupService
                .open(NotificationTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
