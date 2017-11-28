import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    GroupsTcService,
    GroupsTcPopupService,
    GroupsTcComponent,
    GroupsTcDetailComponent,
    GroupsTcDialogComponent,
    GroupsTcPopupComponent,
    GroupsTcDeletePopupComponent,
    GroupsTcDeleteDialogComponent,
    groupsRoute,
    groupsPopupRoute,
    GroupsTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...groupsRoute,
    ...groupsPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GroupsTcComponent,
        GroupsTcDetailComponent,
        GroupsTcDialogComponent,
        GroupsTcDeleteDialogComponent,
        GroupsTcPopupComponent,
        GroupsTcDeletePopupComponent,
    ],
    entryComponents: [
        GroupsTcComponent,
        GroupsTcDialogComponent,
        GroupsTcPopupComponent,
        GroupsTcDeleteDialogComponent,
        GroupsTcDeletePopupComponent,
    ],
    providers: [
        GroupsTcService,
        GroupsTcPopupService,
        GroupsTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringGroupsTcModule {}
