import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { NoteTc } from './note-tc.model';
import { NoteTcPopupService } from './note-tc-popup.service';
import { NoteTcService } from './note-tc.service';

@Component({
    selector: 'jhi-note-tc-delete-dialog',
    templateUrl: './note-tc-delete-dialog.component.html'
})
export class NoteTcDeleteDialogComponent {

    note: NoteTc;

    constructor(
        private noteService: NoteTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.noteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'noteListModification',
                content: 'Deleted an note'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-note-tc-delete-popup',
    template: ''
})
export class NoteTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notePopupService: NoteTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.notePopupService
                .open(NoteTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
