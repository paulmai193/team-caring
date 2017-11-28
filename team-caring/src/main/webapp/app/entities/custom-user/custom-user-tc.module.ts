import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import { TeamCaringAdminModule } from '../../admin/admin.module';
import {
    CustomUserTcService,
    CustomUserTcPopupService,
    CustomUserTcComponent,
    CustomUserTcDetailComponent,
    CustomUserTcDialogComponent,
    CustomUserTcPopupComponent,
    CustomUserTcDeletePopupComponent,
    CustomUserTcDeleteDialogComponent,
    customUserRoute,
    customUserPopupRoute,
    CustomUserTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...customUserRoute,
    ...customUserPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        TeamCaringAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CustomUserTcComponent,
        CustomUserTcDetailComponent,
        CustomUserTcDialogComponent,
        CustomUserTcDeleteDialogComponent,
        CustomUserTcPopupComponent,
        CustomUserTcDeletePopupComponent,
    ],
    entryComponents: [
        CustomUserTcComponent,
        CustomUserTcDialogComponent,
        CustomUserTcPopupComponent,
        CustomUserTcDeleteDialogComponent,
        CustomUserTcDeletePopupComponent,
    ],
    providers: [
        CustomUserTcService,
        CustomUserTcPopupService,
        CustomUserTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringCustomUserTcModule {}
