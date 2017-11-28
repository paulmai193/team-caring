import { BaseEntity } from './../../shared';

export class GroupsMemberTc implements BaseEntity {
    constructor(
        public id?: number,
        public level?: number,
        public members?: BaseEntity[],
        public groups?: BaseEntity[],
    ) {
    }
}
