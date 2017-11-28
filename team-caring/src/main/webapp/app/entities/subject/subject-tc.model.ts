import { BaseEntity } from './../../shared';

export class SubjectTc implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public iconId?: number,
    ) {
    }
}
