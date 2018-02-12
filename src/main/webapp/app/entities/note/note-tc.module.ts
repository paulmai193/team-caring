import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    NoteTcService,
    NoteTcPopupService,
    NoteTcComponent,
    NoteTcDetailComponent,
    NoteTcDialogComponent,
    NoteTcPopupComponent,
    NoteTcDeletePopupComponent,
    NoteTcDeleteDialogComponent,
    noteRoute,
    notePopupRoute,
    NoteTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...noteRoute,
    ...notePopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NoteTcComponent,
        NoteTcDetailComponent,
        NoteTcDialogComponent,
        NoteTcDeleteDialogComponent,
        NoteTcPopupComponent,
        NoteTcDeletePopupComponent,
    ],
    entryComponents: [
        NoteTcComponent,
        NoteTcDialogComponent,
        NoteTcPopupComponent,
        NoteTcDeleteDialogComponent,
        NoteTcDeletePopupComponent,
    ],
    providers: [
        NoteTcService,
        NoteTcPopupService,
        NoteTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringNoteTcModule {}
