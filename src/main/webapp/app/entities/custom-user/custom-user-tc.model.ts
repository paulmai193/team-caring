import { BaseEntity } from './../../shared';

export class CustomUserTc implements BaseEntity {
    constructor(
        public id?: number,
        public fullName?: string,
        public nickname?: string,
        public pushToken?: string,
        public extraGroupName?: string,
        public extraGroupDescription?: string,
        public extraGroupTotalMember?: number,
        public userId?: number,
        public leaders?: BaseEntity[],
        public owners?: BaseEntity[],
        public members?: BaseEntity[],
        public notifications?: BaseEntity[],
        public appointments?: BaseEntity[],
        public attendees?: BaseEntity[],
        public notes?: BaseEntity[],
    ) {
    }
}
