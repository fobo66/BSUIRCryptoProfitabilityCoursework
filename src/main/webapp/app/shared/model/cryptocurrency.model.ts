export interface ICryptocurrency {
  id?: number;
  name?: string;
  shortName?: string;
  price?: number;
}

export class Cryptocurrency implements ICryptocurrency {
  constructor(public id?: number, public name?: string, public shortName?: string, public price?: number) {}
}
