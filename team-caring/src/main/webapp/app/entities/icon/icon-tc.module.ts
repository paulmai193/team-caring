import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    IconTcService,
    IconTcPopupService,
    IconTcComponent,
    IconTcDetailComponent,
    IconTcDialogComponent,
    IconTcPopupComponent,
    IconTcDeletePopupComponent,
    IconTcDeleteDialogComponent,
    iconRoute,
    iconPopupRoute,
    IconTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...iconRoute,
    ...iconPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        IconTcComponent,
        IconTcDetailComponent,
        IconTcDialogComponent,
        IconTcDeleteDialogComponent,
        IconTcPopupComponent,
        IconTcDeletePopupComponent,
    ],
    entryComponents: [
        IconTcComponent,
        IconTcDialogComponent,
        IconTcPopupComponent,
        IconTcDeleteDialogComponent,
        IconTcDeletePopupComponent,
    ],
    providers: [
        IconTcService,
        IconTcPopupService,
        IconTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringIconTcModule {}
