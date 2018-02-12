import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    NotificationTcService,
    NotificationTcPopupService,
    NotificationTcComponent,
    NotificationTcDetailComponent,
    NotificationTcDialogComponent,
    NotificationTcPopupComponent,
    NotificationTcDeletePopupComponent,
    NotificationTcDeleteDialogComponent,
    notificationRoute,
    notificationPopupRoute,
    NotificationTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...notificationRoute,
    ...notificationPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NotificationTcComponent,
        NotificationTcDetailComponent,
        NotificationTcDialogComponent,
        NotificationTcDeleteDialogComponent,
        NotificationTcPopupComponent,
        NotificationTcDeletePopupComponent,
    ],
    entryComponents: [
        NotificationTcComponent,
        NotificationTcDialogComponent,
        NotificationTcPopupComponent,
        NotificationTcDeleteDialogComponent,
        NotificationTcDeletePopupComponent,
    ],
    providers: [
        NotificationTcService,
        NotificationTcPopupService,
        NotificationTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringNotificationTcModule {}
