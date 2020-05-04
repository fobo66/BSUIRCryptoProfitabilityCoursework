import { BaseEntity } from './../../shared';

export class Videocard implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public power?: number,
    ) {
    }
}
