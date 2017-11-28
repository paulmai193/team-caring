import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CustomUserTc } from './custom-user-tc.model';
import { CustomUserTcPopupService } from './custom-user-tc-popup.service';
import { CustomUserTcService } from './custom-user-tc.service';

@Component({
    selector: 'jhi-custom-user-tc-delete-dialog',
    templateUrl: './custom-user-tc-delete-dialog.component.html'
})
export class CustomUserTcDeleteDialogComponent {

    customUser: CustomUserTc;

    constructor(
        private customUserService: CustomUserTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.customUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'customUserListModification',
                content: 'Deleted an customUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-custom-user-tc-delete-popup',
    template: ''
})
export class CustomUserTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private customUserPopupService: CustomUserTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.customUserPopupService
                .open(CustomUserTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
