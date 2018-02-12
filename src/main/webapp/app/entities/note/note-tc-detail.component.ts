import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { NoteTc } from './note-tc.model';
import { NoteTcService } from './note-tc.service';

@Component({
    selector: 'jhi-note-tc-detail',
    templateUrl: './note-tc-detail.component.html'
})
export class NoteTcDetailComponent implements OnInit, OnDestroy {

    note: NoteTc;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private noteService: NoteTcService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNotes();
    }

    load(id) {
        this.noteService.find(id).subscribe((note) => {
            this.note = note;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInNotes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'noteListModification',
            (response) => this.load(this.note.id)
        );
    }
}
