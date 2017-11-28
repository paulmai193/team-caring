import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CustomUserTc } from './custom-user-tc.model';
import { CustomUserTcService } from './custom-user-tc.service';

@Component({
    selector: 'jhi-custom-user-tc-detail',
    templateUrl: './custom-user-tc-detail.component.html'
})
export class CustomUserTcDetailComponent implements OnInit, OnDestroy {

    customUser: CustomUserTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private customUserService: CustomUserTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCustomUsers();
    }

    load(id) {
        this.customUserService.find(id).subscribe((customUser) => {
            this.customUser = customUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCustomUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'customUserListModification',
            (response) => this.load(this.customUser.id)
        );
    }
}
