import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { GroupsMemberTc } from './groups-member-tc.model';
import { GroupsMemberTcPopupService } from './groups-member-tc-popup.service';
import { GroupsMemberTcService } from './groups-member-tc.service';
import { CustomUserTc, CustomUserTcService } from '../custom-user';
import { GroupsTc, GroupsTcService } from '../groups';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-groups-member-tc-dialog',
    templateUrl: './groups-member-tc-dialog.component.html'
})
export class GroupsMemberTcDialogComponent implements OnInit {

    groupsMember: GroupsMemberTc;
    isSaving: boolean;

    customusers: CustomUserTc[];

    groups: GroupsTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private groupsMemberService: GroupsMemberTcService,
        private customUserService: CustomUserTcService,
        private groupsService: GroupsTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customUserService.query()
            .subscribe((res: ResponseWrapper) => { this.customusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupsService.query()
            .subscribe((res: ResponseWrapper) => { this.groups = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.groupsMember.id !== undefined) {
            this.subscribeToSaveResponse(
                this.groupsMemberService.update(this.groupsMember));
        } else {
            this.subscribeToSaveResponse(
                this.groupsMemberService.create(this.groupsMember));
        }
    }

    private subscribeToSaveResponse(result: Observable<GroupsMemberTc>) {
        result.subscribe((res: GroupsMemberTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: GroupsMemberTc) {
        this.eventManager.broadcast({ name: 'groupsMemberListModification', content: 'OK'});
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

    trackGroupsById(index: number, item: GroupsTc) {
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
    selector: 'jhi-groups-member-tc-popup',
    template: ''
})
export class GroupsMemberTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsMemberPopupService: GroupsMemberTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.groupsMemberPopupService
                    .open(GroupsMemberTcDialogComponent as Component, params['id']);
            } else {
                this.groupsMemberPopupService
                    .open(GroupsMemberTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
