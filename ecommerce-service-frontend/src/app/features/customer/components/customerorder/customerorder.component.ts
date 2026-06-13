import {Component, inject, OnInit, signal} from '@angular/core';
import {CustomerorderService} from '../../services/customerorder.service';
import {Router, RouterLink} from '@angular/router';
import {Order} from '../../../../shared/models/order.model';
import {CurrencyPipe, DatePipe} from '@angular/common';

@Component({
  selector: 'app-customerorder',
  imports: [
    DatePipe,
    CurrencyPipe,
    RouterLink
  ],
  templateUrl: './customerorder.component.html',
  styleUrl: './customerorder.component.css',
})
export class CustomerorderComponent implements OnInit{
  private customerOrderService = inject(CustomerorderService);
  userId: number = 1;
  private router = inject(Router);
  customerOrderList= signal<Order[]>([]);
  isLoading = signal<boolean>(false);

  constructor() {}

  ngOnInit(): void {
    this.loadMyPlacedOrders();
  }

  loadMyPlacedOrders(): void {
    this.customerOrderService.getMyPlacedOrders(this.userId).subscribe({
      next: (data) => {
        this.customerOrderList.set(data ?? []);
      },
      error: (err) => {
        if (err.status === 204) {
          this.customerOrderList.set([]);
        } else {
          console.error("Fetch failed", err);
        }
      }
    });
  }
}
