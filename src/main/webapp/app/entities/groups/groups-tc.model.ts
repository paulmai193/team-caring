import { BaseEntity } from './../../shared';

export class GroupsTc implements BaseEntity {
    constructor(
        public id?: number,
        public members?: BaseEntity[],
        public leaderId?: number,
        public teamId?: number,
    ) {
    }
}
