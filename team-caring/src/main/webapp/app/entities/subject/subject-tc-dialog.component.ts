import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SubjectTc } from './subject-tc.model';
import { SubjectTcPopupService } from './subject-tc-popup.service';
import { SubjectTcService } from './subject-tc.service';
import { IconTc, IconTcService } from '../icon';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-subject-tc-dialog',
    templateUrl: './subject-tc-dialog.component.html'
})
export class SubjectTcDialogComponent implements OnInit {

    subject: SubjectTc;
    isSaving: boolean;

    icons: IconTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private subjectService: SubjectTcService,
        private iconService: IconTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.iconService
            .query({filter: 'subject-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.subject.iconId) {
                    this.icons = res.json;
                } else {
                    this.iconService
                        .find(this.subject.iconId)
                        .subscribe((subRes: IconTc) => {
                            this.icons = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.subject.id !== undefined) {
            this.subscribeToSaveResponse(
                this.subjectService.update(this.subject));
        } else {
            this.subscribeToSaveResponse(
                this.subjectService.create(this.subject));
        }
    }

    private subscribeToSaveResponse(result: Observable<SubjectTc>) {
        result.subscribe((res: SubjectTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SubjectTc) {
        this.eventManager.broadcast({ name: 'subjectListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackIconById(index: number, item: IconTc) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-subject-tc-popup',
    template: ''
})
export class SubjectTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subjectPopupService: SubjectTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.subjectPopupService
                    .open(SubjectTcDialogComponent as Component, params['id']);
            } else {
                this.subjectPopupService
                    .open(SubjectTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
