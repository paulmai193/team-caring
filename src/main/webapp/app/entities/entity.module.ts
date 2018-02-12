import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TeamCaringCustomUserTcModule } from './custom-user/custom-user-tc.module';
import { TeamCaringGroupsTcModule } from './groups/groups-tc.module';
import { TeamCaringGroupsMemberTcModule } from './groups-member/groups-member-tc.module';
import { TeamCaringTeamTcModule } from './team/team-tc.module';
import { TeamCaringIconTcModule } from './icon/icon-tc.module';
import { TeamCaringNotificationTcModule } from './notification/notification-tc.module';
import { TeamCaringAppointmentTcModule } from './appointment/appointment-tc.module';
import { TeamCaringAttendeeTcModule } from './attendee/attendee-tc.module';
import { TeamCaringNoteTcModule } from './note/note-tc.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TeamCaringCustomUserTcModule,
        TeamCaringGroupsTcModule,
        TeamCaringGroupsMemberTcModule,
        TeamCaringTeamTcModule,
        TeamCaringIconTcModule,
        TeamCaringNotificationTcModule,
        TeamCaringAppointmentTcModule,
        TeamCaringAttendeeTcModule,
        TeamCaringNoteTcModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TeamCaringEntityModule {}
