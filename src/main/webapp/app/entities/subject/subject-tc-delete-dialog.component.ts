import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SubjectTc } from './subject-tc.model';
import { SubjectTcPopupService } from './subject-tc-popup.service';
import { SubjectTcService } from './subject-tc.service';

@Component({
    selector: 'jhi-subject-tc-delete-dialog',
    templateUrl: './subject-tc-delete-dialog.component.html'
})
export class SubjectTcDeleteDialogComponent {

    subject: SubjectTc;

    constructor(
        private subjectService: SubjectTcService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.subjectService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'subjectListModification',
                content: 'Deleted an subject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-subject-tc-delete-popup',
    template: ''
})
export class SubjectTcDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subjectPopupService: SubjectTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.subjectPopupService
                .open(SubjectTcDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
