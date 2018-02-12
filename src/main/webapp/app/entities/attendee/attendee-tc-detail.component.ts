import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { AttendeeTc } from './attendee-tc.model';
import { AttendeeTcService } from './attendee-tc.service';

@Component({
    selector: 'jhi-attendee-tc-detail',
    templateUrl: './attendee-tc-detail.component.html'
})
export class AttendeeTcDetailComponent implements OnInit, OnDestroy {

    attendee: AttendeeTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private attendeeService: AttendeeTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAttendees();
    }

    load(id) {
        this.attendeeService.find(id).subscribe((attendee) => {
            this.attendee = attendee;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAttendees() {
        this.eventSubscriber = this.eventManager.subscribe(
            'attendeeListModification',
            (response) => this.load(this.attendee.id)
        );
    }
}
