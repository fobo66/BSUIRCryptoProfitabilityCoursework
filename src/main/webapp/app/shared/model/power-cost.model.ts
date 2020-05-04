export interface IPowerCost {
  id?: number;
  city?: string;
  pricePerKilowatt?: number;
}

export class PowerCost implements IPowerCost {
  constructor(public id?: number, public city?: string, public pricePerKilowatt?: number) {}
}
