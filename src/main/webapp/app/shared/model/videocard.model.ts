export interface IVideocard {
  id?: number;
  name?: string;
  power?: number;
}

export class Videocard implements IVideocard {
  constructor(public id?: number, public name?: string, public power?: number) {}
}
