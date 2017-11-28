import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { GroupsTc } from './groups-tc.model';
import { GroupsTcPopupService } from './groups-tc-popup.service';
import { GroupsTcService } from './groups-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { GroupsMemberTc, GroupsMemberTcService } from '../groups-member';
import { TeamTc, TeamTcService } from '../team';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-groups-tc-dialog',
    templateUrl: './groups-tc-dialog.component.html'
})
export class GroupsTcDialogComponent implements OnInit {

    groups: GroupsTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    groupsmembers: GroupsMemberTc[];

    teams: TeamTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private groupsService: GroupsTcService,
        private customUserService: CustomUserTcService,
        private groupsMemberService: GroupsMemberTcService,
        private teamService: TeamTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupsMemberService.query()
            .subscribe((res: ResponseWrapper) => { this.groupsmembers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.teamService.query()
            .subscribe((res: ResponseWrapper) => { this.teams = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.groups.id !== undefined) {
            this.subscribeToSaveResponse(
                this.groupsService.update(this.groups));
        } else {
            this.subscribeToSaveResponse(
                this.groupsService.create(this.groups));
        }
    }

    private subscribeToSaveResponse(result: Observable<GroupsTc>) {
        result.subscribe((res: GroupsTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: GroupsTc) {
        this.eventManager.broadcast({ name: 'groupsListModification', content: 'OK'});
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

    trackGroupsMemberById(index: number, item: GroupsMemberTc) {
        return item.id;
    }

    trackTeamById(index: number, item: TeamTc) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-groups-tc-popup',
    template: ''
})
export class GroupsTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsPopupService: GroupsTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.groupsPopupService
                    .open(GroupsTcDialogComponent as Component, params['id']);
            } else {
                this.groupsPopupService
                    .open(GroupsTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
