import { BaseEntity } from './../../shared';

export class HardwareInfo implements BaseEntity {
    constructor(
        public id?: number,
        public hashPower?: number,
        public price?: number,
        public videocard?: BaseEntity,
    ) {
    }
}
