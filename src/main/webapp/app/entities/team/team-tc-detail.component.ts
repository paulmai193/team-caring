import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TeamTc } from './team-tc.model';
import { TeamTcService } from './team-tc.service';

@Component({
    selector: 'jhi-team-tc-detail',
    templateUrl: './team-tc-detail.component.html'
})
export class TeamTcDetailComponent implements OnInit, OnDestroy {

    team: TeamTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamService: TeamTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeams();
    }

    load(id) {
        this.teamService.find(id).subscribe((team) => {
            this.team = team;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeams() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamListModification',
            (response) => this.load(this.team.id)
        );
    }
}
