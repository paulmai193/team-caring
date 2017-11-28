import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IconTc } from './icon-tc.model';
import { IconTcPopupService } from './icon-tc-popup.service';
import { IconTcService } from './icon-tc.service';

@Component({
    selector: 'jhi-icon-tc-delete-dialog',
    templateUrl: './icon-tc-delete-dialog.component.html'
})
export class IconTcDeleteDialogComponent {

    icon: IconTc;

    constructor(
        private iconService: IconTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.iconService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'iconListModification',
                content: 'Deleted an icon'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-icon-tc-delete-popup',
    template: ''
})
export class IconTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private iconPopupService: IconTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.iconPopupService
                .open(IconTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
