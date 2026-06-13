import { Injectable } from '@angular/core';
// import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
// import {Category} from '../../../shared/models/category.model';
import {Product} from '../../../shared/models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly API_URL = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  addNewProduct(product:Product) : Observable<Product>{
    const formData = new FormData();
    formData.append("name", product.name);
    formData.append("description", product.description);
    formData.append("price", product.price.toString());
    formData.append("quantity", product.quantity.toString());
    formData.append("isActive", "true");
    formData.append("categoryId", product.categoryId.toString());

    if(product.byteImg instanceof File){
      formData.append("img", product.byteImg);
    }
    return this.http.post<Product>(`${this.API_URL}/product`, formData);
  }

  updateProduct(productId:number, product:Product) : Observable<Product>{
    const updateFormData = new FormData();
    updateFormData.append("name", product.name);
    updateFormData.append("description", product.description);
    updateFormData.append("price", product.price.toString());
    updateFormData.append("quantity", product.quantity.toString());
    updateFormData.append("isActive", product.isActive.toString());
    updateFormData.append("categoryId", product.categoryId.toString());

    if(product.byteImg instanceof File){
      updateFormData.append("img", product.byteImg);
    }
    return this.http.put<Product>(`${this.API_URL}/product/${productId}`, updateFormData);
  }

  getAllProductsForAdmin() : Observable<Product[]>{
    return this.http.get<Product[]>(`${this.API_URL}/products`);
  }

  getProductById(productId:number) : Observable<Product>{
    return this.http.get<Product>(`${this.API_URL}/product/${productId}`);
  }

  searchProductByName(name:String) : Observable<Product>{
    return this.http.get<Product>(`${this.API_URL}/search/${name}`);
  }

  addStock(productId:number, qty:number) : Observable<Product>{
    let qtyParam = new HttpParams().set('quantity', qty.toString());
    return this.http.put<Product>(`${this.API_URL}/${productId}/add-stock`, null, { params: qtyParam });
  }

  deleteProduct(productId:number) : Observable<Product>{
    return this.http.delete<Product>(`${this.API_URL}/product/${productId}`);
  }
}
