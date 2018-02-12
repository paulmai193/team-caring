import { BaseEntity } from './../../shared';

export class TeamTc implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public level?: number,
        public totalMember?: number,
        public extraGroupName?: string,
        public extraGroupDescription?: string,
        public extraGroupTotalMember?: number,
        public groups?: BaseEntity[],
        public appointments?: BaseEntity[],
        public ownerId?: number,
        public iconId?: number,
    ) {
    }
}
