import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    TeamTcService,
    TeamTcPopupService,
    TeamTcComponent,
    TeamTcDetailComponent,
    TeamTcDialogComponent,
    TeamTcPopupComponent,
    TeamTcDeletePopupComponent,
    TeamTcDeleteDialogComponent,
    teamRoute,
    teamPopupRoute,
    TeamTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...teamRoute,
    ...teamPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TeamTcComponent,
        TeamTcDetailComponent,
        TeamTcDialogComponent,
        TeamTcDeleteDialogComponent,
        TeamTcPopupComponent,
        TeamTcDeletePopupComponent,
    ],
    entryComponents: [
        TeamTcComponent,
        TeamTcDialogComponent,
        TeamTcPopupComponent,
        TeamTcDeleteDialogComponent,
        TeamTcDeletePopupComponent,
    ],
    providers: [
        TeamTcService,
        TeamTcPopupService,
        TeamTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringTeamTcModule {}
