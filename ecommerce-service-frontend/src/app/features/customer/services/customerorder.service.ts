import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Order} from '../../../shared/models/order.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerorderService {
  private readonly API_URL = API_ENDPOINTS.customerOrder;
  constructor(private http: HttpClient) {}

  getMyPlacedOrders(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.API_URL}/myorders/${userId}`);
  }

  private getHeader() {
    const jwtToken = window.localStorage.getItem('token');
    return {
      headers: {
        'Authorization': 'Bearer ' + jwtToken
      }
    };
  }

  getOrderById(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.API_URL}/details/${orderId}`, this.getHeader());
  }
}
