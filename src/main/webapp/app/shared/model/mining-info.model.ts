import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';

export interface IMiningInfo {
  id?: number;
  difficulty?: number;
  blockReward?: number;
  cryptocurrency?: ICryptocurrency;
}

export class MiningInfo implements IMiningInfo {
  constructor(public id?: number, public difficulty?: number, public blockReward?: number, public cryptocurrency?: ICryptocurrency) {}
}
