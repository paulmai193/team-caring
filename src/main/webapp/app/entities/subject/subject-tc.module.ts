import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeamCaringSharedModule } from '../../shared';
import {
    SubjectTcService,
    SubjectTcPopupService,
    SubjectTcComponent,
    SubjectTcDetailComponent,
    SubjectTcDialogComponent,
    SubjectTcPopupComponent,
    SubjectTcDeletePopupComponent,
    SubjectTcDeleteDialogComponent,
    subjectRoute,
    subjectPopupRoute,
    SubjectTcResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...subjectRoute,
    ...subjectPopupRoute,
];

@NgModule({
    imports: [
        TeamCaringSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SubjectTcComponent,
        SubjectTcDetailComponent,
        SubjectTcDialogComponent,
        SubjectTcDeleteDialogComponent,
        SubjectTcPopupComponent,
        SubjectTcDeletePopupComponent,
    ],
    entryComponents: [
        SubjectTcComponent,
        SubjectTcDialogComponent,
        SubjectTcPopupComponent,
        SubjectTcDeleteDialogComponent,
        SubjectTcDeletePopupComponent,
    ],
    providers: [
        SubjectTcService,
        SubjectTcPopupService,
        SubjectTcResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringSubjectTcModule {}
