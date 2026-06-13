import {Component, inject, OnInit, signal} from '@angular/core';
import {CustomerorderService} from '../../../customer/services/customerorder.service';
import {Router} from '@angular/router';
import {Order} from '../../../../shared/models/order.model';
import {AdminorderService} from '../../services/adminorder.service';
import {CurrencyPipe, DatePipe} from '@angular/common';

@Component({
  selector: 'app-adminorder',
  imports: [
    CurrencyPipe,
    DatePipe
  ],
  templateUrl: './adminorder.component.html',
  styleUrl: './adminorder.component.css',
})
export class AdminorderComponent implements OnInit{
  private adminorderService = inject(AdminorderService);
  userId: number = 1;
  private router = inject(Router);
  isLoading = signal<boolean>(true);
  adminOrderList= signal<Order[]>([]);

  constructor() {}

  ngOnInit(): void {
    this.loadAllPlacedOrders();
  }

  loadAllPlacedOrders(): void {
    this.isLoading.set(true);
    this.adminorderService.getAllPlacedOrders().subscribe({
      next: (data) => {
        this.adminOrderList.set(data ?? []);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.isLoading.set(false);
        if (err.status === 204) {
          this.adminOrderList.set([]);
        } else {
          console.error("Fetch failed", err);
        }
      }
    });
  }

  viewDetails(orderId: number) {
    this.router.navigate(['/admin/order', orderId]);
  }

  updateStatus(orderId: number, newStatus: string): void {
    if (confirm(`Are you sure you want to mark order #${orderId} as ${newStatus}?`)) {
      this.adminorderService.updateStatus(orderId, newStatus).subscribe({
        next: (updatedOrder) => {
          this.adminOrderList.update(orders =>
            orders.map(o => o.id === orderId ? { ...o, status: newStatus } : o)
          );
          alert(`Order status updated to ${newStatus}`);
        },
        error: (err) => {
          console.error("Update failed", err);
          alert("Could not update status. Please check backend logs.");
        }
      });
    }
  }
}
