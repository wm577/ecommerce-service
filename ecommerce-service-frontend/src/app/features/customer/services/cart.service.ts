import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Product} from '../../../shared/models/product.model';
import {CartResponseDto} from '../../../shared/models/cart.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private readonly API_URL = API_ENDPOINTS.customerCart;
  private readonly API_URL1 = API_ENDPOINTS.customerOrder;

  constructor(private http: HttpClient) {}

  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject;

  addProductToCart(userId:number, productId:number) : Observable<String>{
    return this.http.post(`${this.API_URL}/add/${userId}/${productId}`, {}, {
      responseType: 'text'});
  }

  getCartItemsByUserId(userId:number) : Observable<CartResponseDto>{
    return this.http.get<CartResponseDto>(`${this.API_URL}/${userId}`);
  }

  checkOutCartItem(userId:number, couponCode?:string){
    let params = new HttpParams();

    if (couponCode && couponCode.trim() !== "") {
      params = params.set('couponCode', couponCode);
    }
    return this.http.post(`${this.API_URL1}/checkout/${userId}?couponCode=${couponCode}`, {});
  }

  removeCartItem(userId:number, productId:number){
    return this.http.delete(`${this.API_URL}/remove/${userId}/${productId}`, {
      responseType: 'text'
    });
  }

  clearCart(userId:number){
    return this.http.delete(`${this.API_URL}/clear/${userId}`, {
      responseType: 'text'
    });
  }
}
