import {Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AnalyticsResponseDto} from '../models/analytics-response-dto.model';
import {catchError, Observable, of, tap} from 'rxjs';
import {API_ENDPOINTS} from '../../../core/Config';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  private API_URL = API_ENDPOINTS.adminAnalytics;
  constructor(private http: HttpClient) {}

  summary = signal<AnalyticsResponseDto | null>(null);

  getSummary(): Observable<AnalyticsResponseDto> {
    return this.http.get<AnalyticsResponseDto>(`${this.API_URL}/summary`).pipe(
      tap(data => this.summary.set(data)),
      catchError(err => {
        console.error('Backend Query Failed:', err);

        const emptyState: AnalyticsResponseDto = {
          currentMonthOrders: 0,
          currentMonthEarnings: 0,
          previousMonthOrders: 0,
          previousMonthEarnings: 0,
          orderGrowth: 0,
          earningGrowth: 0,
          averageOrderValue: 0,
          placed: 0,
          shipped: 0,
          delivered: 0
        };

        return of(emptyState);
      })
    );
  }
}
