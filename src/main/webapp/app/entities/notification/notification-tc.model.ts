import { BaseEntity } from './../../shared';

export class NotificationTc implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public message?: string,
        public read?: boolean,
        public type?: number,
        public targetId?: number,
        public customUserId?: number,
    ) {
        this.read = false;
    }
}
