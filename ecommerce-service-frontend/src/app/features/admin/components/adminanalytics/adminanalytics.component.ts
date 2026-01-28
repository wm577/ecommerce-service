import {ChangeDetectorRef, Component, computed, inject, NgZone, OnInit, signal} from '@angular/core';
import {AnalyticsResponseDto} from '../../models/analytics-response-dto.model';
import {AnalyticsService} from '../../services/analytics.service';
import {CommonModule, CurrencyPipe, DecimalPipe} from '@angular/common';

@Component({
  selector: 'app-adminanalytics',
  imports: [
    CurrencyPipe,
    DecimalPipe,
    CommonModule
  ],
  templateUrl: './adminanalytics.component.html',
  styleUrl: './adminanalytics.component.css',
})
export class AdminanalyticsComponent implements OnInit{
  analyticsService = inject(AnalyticsService);

  // This solves TS2339 and updates automatically
  totalPending = computed(() => {
    const data = this.analyticsService.summary();
    if (!data) return 0;
    // (1 Placed + 1 Shipped) = 2
    return (data.placed || 0) + (data.shipped || 0);
  });

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.analyticsService.getSummary().subscribe();
  }
}
