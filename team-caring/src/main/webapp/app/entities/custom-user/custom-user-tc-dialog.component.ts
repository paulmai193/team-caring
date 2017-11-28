import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CustomUserTc } from './custom-user-tc.model';
import { CustomUserTcPopupService } from './custom-user-tc-popup.service';
import { CustomUserTcService } from './custom-user-tc.service';
import { User, UserService } from '../../shared';
import { GroupsMemberTc, GroupsMemberTcService } from '../groups-member';
import { GroupsTc, GroupsTcService } from '../groups';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-custom-user-tc-dialog',
    templateUrl: './custom-user-tc-dialog.component.html'
})
export class CustomUserTcDialogComponent implements OnInit {

    customUser: CustomUserTc;
    isSaving: boolean;

    users: User[];

    groupsmembers: GroupsMemberTc[];

    groups: GroupsTc[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private customUserService: CustomUserTcService,
        private userService: UserService,
        private groupsMemberService: GroupsMemberTcService,
        private groupsService: GroupsTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupsMemberService.query()
            .subscribe((res: ResponseWrapper) => { this.groupsmembers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.groupsService.query()
            .subscribe((res: ResponseWrapper) => { this.groups = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.customUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.customUserService.update(this.customUser));
        } else {
            this.subscribeToSaveResponse(
                this.customUserService.create(this.customUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<CustomUserTc>) {
        result.subscribe((res: CustomUserTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CustomUserTc) {
        this.eventManager.broadcast({ name: 'customUserListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackGroupsMemberById(index: number, item: GroupsMemberTc) {
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
    selector: 'jhi-custom-user-tc-popup',
    template: ''
})
export class CustomUserTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private customUserPopupService: CustomUserTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.customUserPopupService
                    .open(CustomUserTcDialogComponent as Component, params['id']);
            } else {
                this.customUserPopupService
                    .open(CustomUserTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
