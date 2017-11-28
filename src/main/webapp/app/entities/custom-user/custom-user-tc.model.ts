import { BaseEntity } from './../../shared';

export class CustomUserTc implements BaseEntity {
    constructor(
        public id?: number,
        public fullName?: string,
        public nickname?: string,
        public pushToken?: string,
        public userId?: number,
        public leaders?: BaseEntity[],
        public members?: BaseEntity[],
    ) {
    }
}
