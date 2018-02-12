import { BaseEntity } from './../../shared';

export class NoteTc implements BaseEntity {
    constructor(
        public id?: number,
        public general?: string,
        public separate?: string,
        public reminder?: string,
        public customUserId?: number,
        public appointmentId?: number,
    ) {
    }
}
