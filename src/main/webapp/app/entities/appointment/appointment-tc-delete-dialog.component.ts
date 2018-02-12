import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AppointmentTc } from './appointment-tc.model';
import { AppointmentTcPopupService } from './appointment-tc-popup.service';
import { AppointmentTcService } from './appointment-tc.service';

@Component({
    selector: 'jhi-appointment-tc-delete-dialog',
    templateUrl: './appointment-tc-delete-dialog.component.html'
})
export class AppointmentTcDeleteDialogComponent {

    appointment: AppointmentTc;

    constructor(
        private appointmentService: AppointmentTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appointmentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appointmentListModification',
                content: 'Deleted an appointment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-appointment-tc-delete-popup',
    template: ''
})
export class AppointmentTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appointmentPopupService: AppointmentTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appointmentPopupService
                .open(AppointmentTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
