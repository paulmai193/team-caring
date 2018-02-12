import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationTc } from './notification-tc.model';
import { NotificationTcService } from './notification-tc.service';

@Component({
    selector: 'jhi-notification-tc-detail',
    templateUrl: './notification-tc-detail.component.html'
})
export class NotificationTcDetailComponent implements OnInit, OnDestroy {

    notification: NotificationTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private notificationService: NotificationTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNotifications();
    }

    load(id) {
        this.notificationService.find(id).subscribe((notification) => {
            this.notification = notification;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInNotifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'notificationListModification',
            (response) => this.load(this.notification.id)
        );
    }
}
