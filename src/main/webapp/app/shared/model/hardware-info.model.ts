import { IVideocard } from 'app/shared/model/videocard.model';

export interface IHardwareInfo {
  id?: number;
  hashPower?: number;
  price?: number;
  videocard?: IVideocard;
}

export class HardwareInfo implements IHardwareInfo {
  constructor(public id?: number, public hashPower?: number, public price?: number, public videocard?: IVideocard) {}
}
