import { Injectable } from '@angular/core';
import {API_ENDPOINTS} from '../../../core/Config';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Order} from '../../../shared/models/order.model';

@Injectable({
  providedIn: 'root',
})
export class AdminorderService {
  private readonly API_URL = API_ENDPOINTS.adminOrder;
  constructor(private http: HttpClient) {}

  getAllPlacedOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.API_URL}/placed`);
  }

  updateStatus(orderId: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.put(`${this.API_URL}/${orderId}/status`, {}, { params });
  }

  getOrderById(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.API_URL}/${orderId}`);
  }
}
