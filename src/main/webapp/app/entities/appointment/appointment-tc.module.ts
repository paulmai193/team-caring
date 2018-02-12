import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    AppointmentTcService,
    AppointmentTcPopupService,
    AppointmentTcComponent,
    AppointmentTcDetailComponent,
    AppointmentTcDialogComponent,
    AppointmentTcPopupComponent,
    AppointmentTcDeletePopupComponent,
    AppointmentTcDeleteDialogComponent,
    appointmentRoute,
    appointmentPopupRoute,
    AppointmentTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...appointmentRoute,
    ...appointmentPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AppointmentTcComponent,
        AppointmentTcDetailComponent,
        AppointmentTcDialogComponent,
        AppointmentTcDeleteDialogComponent,
        AppointmentTcPopupComponent,
        AppointmentTcDeletePopupComponent,
    ],
    entryComponents: [
        AppointmentTcComponent,
        AppointmentTcDialogComponent,
        AppointmentTcPopupComponent,
        AppointmentTcDeleteDialogComponent,
        AppointmentTcDeletePopupComponent,
    ],
    providers: [
        AppointmentTcService,
        AppointmentTcPopupService,
        AppointmentTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringAppointmentTcModule {}
