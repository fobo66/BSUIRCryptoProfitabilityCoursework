import { BaseEntity } from './../../shared';

export class Cryptocurrency implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortName?: string,
        public price?: number,
    ) {
    }
}
