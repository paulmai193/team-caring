import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    AttendeeTcService,
    AttendeeTcPopupService,
    AttendeeTcComponent,
    AttendeeTcDetailComponent,
    AttendeeTcDialogComponent,
    AttendeeTcPopupComponent,
    AttendeeTcDeletePopupComponent,
    AttendeeTcDeleteDialogComponent,
    attendeeRoute,
    attendeePopupRoute,
    AttendeeTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...attendeeRoute,
    ...attendeePopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AttendeeTcComponent,
        AttendeeTcDetailComponent,
        AttendeeTcDialogComponent,
        AttendeeTcDeleteDialogComponent,
        AttendeeTcPopupComponent,
        AttendeeTcDeletePopupComponent,
    ],
    entryComponents: [
        AttendeeTcComponent,
        AttendeeTcDialogComponent,
        AttendeeTcPopupComponent,
        AttendeeTcDeleteDialogComponent,
        AttendeeTcDeletePopupComponent,
    ],
    providers: [
        AttendeeTcService,
        AttendeeTcPopupService,
        AttendeeTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringAttendeeTcModule {}
