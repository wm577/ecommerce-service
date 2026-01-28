import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../../../shared/models/product.model';
import {Coupon} from '../../../shared/models/coupon.model';

@Injectable({
  providedIn: 'root',
})
export class CouponService {
  private readonly API_URL = API_ENDPOINTS.adminCoupon;

  constructor(private http: HttpClient) {}

  addNewCoupon(coupon:Coupon) : Observable<Coupon>{
    // const formData = new FormData();
    // formData.append("name", coupon.name);
    // formData.append("code", coupon.code);
    // formData.append("discount", coupon.discount.toString());
    //
    // const dateObj = new Date(coupon.expirationDate);
    // formData.append("expirationDate", dateObj.toISOString());

    return this.http.post<Coupon>(this.API_URL, coupon);
  }

  getAllCouponsForAdmin() : Observable<Coupon[]>{
    return this.http.get<Coupon[]>(`${this.API_URL}`);
  }
}
