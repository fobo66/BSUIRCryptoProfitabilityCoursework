import { BaseEntity } from './../../shared';

export class PowerCost implements BaseEntity {
    constructor(
        public id?: number,
        public city?: string,
        public pricePerKilowatt?: number,
    ) {
    }
}
