import { BaseEntity } from './../../shared';

export class GroupsMemberTc implements BaseEntity {
    constructor(
        public id?: number,
        public level?: number,
        public status?: number,
        public customUserId?: number,
        public groupsId?: number,
    ) {
    }
}
