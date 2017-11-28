import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsMemberTc } from './groups-member-tc.model';
import { GroupsMemberTcService } from './groups-member-tc.service';

@Component({
    selector: 'jhi-groups-member-tc-detail',
    templateUrl: './groups-member-tc-detail.component.html'
})
export class GroupsMemberTcDetailComponent implements OnInit, OnDestroy {

    groupsMember: GroupsMemberTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private groupsMemberService: GroupsMemberTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGroupsMembers();
    }

    load(id) {
        this.groupsMemberService.find(id).subscribe((groupsMember) => {
            this.groupsMember = groupsMember;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGroupsMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'groupsMemberListModification',
            (response) => this.load(this.groupsMember.id)
        );
    }
}
