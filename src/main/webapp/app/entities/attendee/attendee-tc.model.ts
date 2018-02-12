import { BaseEntity } from './../../shared';

export class AttendeeTc implements BaseEntity {
    constructor(
        public id?: number,
        public status?: number,
        public customUserId?: number,
        public appointmentId?: number,
    ) {
    }
}
