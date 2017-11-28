import { BaseEntity } from './../../shared';

export class TeamTc implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public level?: number,
        public iconId?: number,
        public groups?: BaseEntity[],
    ) {
    }
}
