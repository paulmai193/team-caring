import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamTc } from './team-tc.model';
import { TeamTcPopupService } from './team-tc-popup.service';
import { TeamTcService } from './team-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { IconTc, IconTcService } from '../icon';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-team-tc-dialog',
    templateUrl: './team-tc-dialog.component.html'
})
export class TeamTcDialogComponent implements OnInit {

    team: TeamTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    icons: IconTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamService: TeamTcService,
        private customUserService: CustomUserTcService,
        private iconService: IconTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.iconService.query()
            .subscribe((res: ResponseWrapper) => { this.icons = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    trackCustomUserById(index: number, item: CustomUserTc) {
        return item.id;
    }

    trackIconById(index: number, item: IconTc) {
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
