import {BaseEntity} from './../../shared';

export class MiningInfo implements BaseEntity {
    constructor(public id?: number,
                public difficulty?: number,
                public blockReward?: number,
                public cryptocurrency?: BaseEntity,) {
    }
}
