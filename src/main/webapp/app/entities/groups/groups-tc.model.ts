import { BaseEntity } from './../../shared';

export class GroupsTc implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public offical?: boolean,
        public totalMember?: number,
        public customUserId?: number,
        public members?: BaseEntity[],
        public teamId?: number,
    ) {
        this.offical = false;
    }
}