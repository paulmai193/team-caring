import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamTc } from './team-tc.model';
import { TeamTcPopupService } from './team-tc-popup.service';
import { TeamTcService } from './team-tc.service';
import { SubjectTc, SubjectTcService } from '../subject';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-team-tc-dialog',
    templateUrl: './team-tc-dialog.component.html'
})
export class TeamTcDialogComponent implements OnInit {

    team: TeamTc;
    isSaving: boolean;

    subjects: SubjectTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamService: TeamTcService,
        private subjectService: SubjectTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.subjectService
            .query({filter: 'team-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.team.subjectId) {
                    this.subjects = res.json;
                } else {
                    this.subjectService
                        .find(this.team.subjectId)
                        .subscribe((subRes: SubjectTc) => {
                            this.subjects = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.team.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamService.update(this.team));
        } else {
            this.subscribeToSaveResponse(
                this.teamService.create(this.team));
        }
    }

    private subscribeToSaveResponse(result: Observable<TeamTc>) {
        result.subscribe((res: TeamTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamTc) {
        this.eventManager.broadcast({ name: 'teamListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSubjectById(index: number, item: SubjectTc) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-team-tc-popup',
    template: ''
})
export class TeamTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamPopupService: TeamTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamPopupService
                    .open(TeamTcDialogComponent as Component, params['id']);
            } else {
                this.teamPopupService
                    .open(TeamTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
