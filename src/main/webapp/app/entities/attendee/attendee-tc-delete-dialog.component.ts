import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AttendeeTc } from './attendee-tc.model';
import { AttendeeTcPopupService } from './attendee-tc-popup.service';
import { AttendeeTcService } from './attendee-tc.service';

@Component({
    selector: 'jhi-attendee-tc-delete-dialog',
    templateUrl: './attendee-tc-delete-dialog.component.html'
})
export class AttendeeTcDeleteDialogComponent {

    attendee: AttendeeTc;

    constructor(
        private attendeeService: AttendeeTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attendeeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'attendeeListModification',
                content: 'Deleted an attendee'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-attendee-tc-delete-popup',
    template: ''
})
export class AttendeeTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attendeePopupService: AttendeeTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.attendeePopupService
                .open(AttendeeTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
