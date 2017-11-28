import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IconTc } from './icon-tc.model';
import { IconTcPopupService } from './icon-tc-popup.service';
import { IconTcService } from './icon-tc.service';

@Component({
    selector: 'jhi-icon-tc-dialog',
    templateUrl: './icon-tc-dialog.component.html'
})
export class IconTcDialogComponent implements OnInit {

    icon: IconTc;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private iconService: IconTcService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.icon.id !== undefined) {
            this.subscribeToSaveResponse(
                this.iconService.update(this.icon));
        } else {
            this.subscribeToSaveResponse(
                this.iconService.create(this.icon));
        }
    }

    private subscribeToSaveResponse(result: Observable<IconTc>) {
        result.subscribe((res: IconTc) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: IconTc) {
        this.eventManager.broadcast({ name: 'iconListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-icon-tc-popup',
    template: ''
})
export class IconTcPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private iconPopupService: IconTcPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.iconPopupService
                    .open(IconTcDialogComponent as Component, params['id']);
            } else {
                this.iconPopupService
                    .open(IconTcDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
