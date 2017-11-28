import { BaseEntity } from './../../shared';

export class CustomUserTc implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public members?: BaseEntity[],
        public groupsId?: number,
    ) {
    }
}
