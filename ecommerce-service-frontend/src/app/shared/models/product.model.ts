import {Category} from './category.model';

export interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  isActive: Boolean;
  byteImg?: any;

  categoryId: number;
  categoryName: string;
}
