import { BaseEntity } from './../../shared';

export class AppointmentTc implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public repeatType?: number,
        public time?: any,
        public customUserId?: number,
        public teamId?: number,
        public attendees?: BaseEntity[],
        public notes?: BaseEntity[],
    ) {
    }
}
