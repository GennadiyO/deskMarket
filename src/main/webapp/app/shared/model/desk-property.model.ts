import { DeskPropertyType } from 'app/shared/model/enumerations/desk-property-type.model';

export interface IDeskProperty {
  id?: number;
  deskPropertyId?: number;
  type?: DeskPropertyType;
  value?: string;
  deskDeskId?: string;
  deskId?: number;
}

export const defaultValue: Readonly<IDeskProperty> = {};
