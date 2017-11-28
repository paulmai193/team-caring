import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    GroupsMemberTcService,
    GroupsMemberTcPopupService,
    GroupsMemberTcComponent,
    GroupsMemberTcDetailComponent,
    GroupsMemberTcDialogComponent,
    GroupsMemberTcPopupComponent,
    GroupsMemberTcDeletePopupComponent,
    GroupsMemberTcDeleteDialogComponent,
    groupsMemberRoute,
    groupsMemberPopupRoute,
    GroupsMemberTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...groupsMemberRoute,
    ...groupsMemberPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GroupsMemberTcComponent,
        GroupsMemberTcDetailComponent,
        GroupsMemberTcDialogComponent,
        GroupsMemberTcDeleteDialogComponent,
        GroupsMemberTcPopupComponent,
        GroupsMemberTcDeletePopupComponent,
    ],
    entryComponents: [
        GroupsMemberTcComponent,
        GroupsMemberTcDialogComponent,
        GroupsMemberTcPopupComponent,
        GroupsMemberTcDeleteDialogComponent,
        GroupsMemberTcDeletePopupComponent,
    ],
    providers: [
        GroupsMemberTcService,
        GroupsMemberTcPopupService,
        GroupsMemberTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringGroupsMemberTcModule {}
