import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../../../shared/models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductlistService {
  private readonly API_URL = API_ENDPOINTS.customerProduct;

  constructor(private http: HttpClient) {}

  getAllProductsForCustomer() : Observable<Product[]>{
    return this.http.get<Product[]>(`${this.API_URL}/products`);
  }

  searchProductByName(name: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/search/${name}`);
  }

  getProductDetailsById(productId:number) : Observable<any>{
    return this.http.get<any>(`${this.API_URL}/product/${productId}`);
  }
}
