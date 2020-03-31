import { Moment } from 'moment';

export interface IPrice {
  id?: number;
  priceId?: number;
  creationDate?: Moment;
  price?: number;
  deskDeskId?: string;
  deskId?: number;
}

export const defaultValue: Readonly<IPrice> = {};
