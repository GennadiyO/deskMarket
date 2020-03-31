import { Moment } from 'moment';
import { IPhoto } from 'app/shared/model/photo.model';
import { IPrice } from 'app/shared/model/price.model';
import { IDeskProperty } from 'app/shared/model/desk-property.model';
import { DeskType } from 'app/shared/model/enumerations/desk-type.model';

export interface IDesk {
  id?: number;
  deskId?: number;
  type?: DeskType;
  name?: string;
  description?: string;
  creationDate?: Moment;
  modificationDate?: Moment;
  photos?: IPhoto[];
  prices?: IPrice[];
  properties?: IDeskProperty[];
}

export const defaultValue: Readonly<IDesk> = {};
