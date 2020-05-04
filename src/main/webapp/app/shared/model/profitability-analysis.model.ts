import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IProfitabilityAnalysis {
  id?: number;
  date?: Moment;
  result?: boolean;
  user?: IUser;
}

export class ProfitabilityAnalysis implements IProfitabilityAnalysis {
  constructor(public id?: number, public date?: Moment, public result?: boolean, public user?: IUser) {
    this.result = this.result || false;
  }
}
