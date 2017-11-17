import {BaseEntity, User} from './../../shared';

export class ProfitabilityAnalysis implements BaseEntity {
    constructor(public id?: number,
                public date?: any,
                public result?: boolean,
                public user?: User,) {
        this.result = false;
    }
}
