import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsMemberTc } from './groups-member-tc.model';
import { GroupsMemberTcPopupService } from './groups-member-tc-popup.service';
import { GroupsMemberTcService } from './groups-member-tc.service';

@Component({
    selector: 'jhi-groups-member-tc-delete-dialog',
    templateUrl: './groups-member-tc-delete-dialog.component.html'
})
export class GroupsMemberTcDeleteDialogComponent {

    groupsMember: GroupsMemberTc;

    constructor(
        private groupsMemberService: GroupsMemberTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.groupsMemberService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'groupsMemberListModification',
                content: 'Deleted an groupsMember'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-groups-member-tc-delete-popup',
    template: ''
})
export class GroupsMemberTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsMemberPopupService: GroupsMemberTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.groupsMemberPopupService
                .open(GroupsMemberTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
