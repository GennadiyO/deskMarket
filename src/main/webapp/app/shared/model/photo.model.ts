export interface IPhoto {
  id?: number;
  photoId?: number;
  photoContentType?: string;
  photo?: any;
  deskDeskId?: string;
  deskId?: number;
}

export const defaultValue: Readonly<IPhoto> = {};
