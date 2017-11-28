import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { IconTc } from './icon-tc.model';
import { IconTcService } from './icon-tc.service';

@Component({
    selector: 'jhi-icon-tc-detail',
    templateUrl: './icon-tc-detail.component.html'
})
export class IconTcDetailComponent implements OnInit, OnDestroy {

    icon: IconTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private iconService: IconTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIcons();
    }

    load(id) {
        this.iconService.find(id).subscribe((icon) => {
            this.icon = icon;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIcons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'iconListModification',
            (response) => this.load(this.icon.id)
        );
    }
}
