import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsTc } from './groups-tc.model';
import { GroupsTcPopupService } from './groups-tc-popup.service';
import { GroupsTcService } from './groups-tc.service';

@Component({
    selector: 'jhi-groups-tc-delete-dialog',
    templateUrl: './groups-tc-delete-dialog.component.html'
})
export class GroupsTcDeleteDialogComponent {

    groups: GroupsTc;

    constructor(
        private groupsService: GroupsTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.groupsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'groupsListModification',
                content: 'Deleted an groups'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-groups-tc-delete-popup',
    template: ''
})
export class GroupsTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsPopupService: GroupsTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.groupsPopupService
                .open(GroupsTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
