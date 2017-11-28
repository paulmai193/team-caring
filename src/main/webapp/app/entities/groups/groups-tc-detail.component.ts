import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsTc } from './groups-tc.model';
import { GroupsTcService } from './groups-tc.service';

@Component({
    selector: 'jhi-groups-tc-detail',
    templateUrl: './groups-tc-detail.component.html'
})
export class GroupsTcDetailComponent implements OnInit, OnDestroy {

    groups: GroupsTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private groupsService: GroupsTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGroups();
    }

    load(id) {
        this.groupsService.find(id).subscribe((groups) => {
            this.groups = groups;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'groupsListModification',
            (response) => this.load(this.groups.id)
        );
    }
}
