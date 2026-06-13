import {Component, inject, OnInit, signal} from '@angular/core';
import {CommonModule, CurrencyPipe, DatePipe} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {AdminorderService} from '../../services/adminorder.service';
import {Order} from '../../../../shared/models/order.model';

@Component({
  selector: 'app-adminorderdetails',
  imports: [
    CommonModule,
  ],
  templateUrl: './adminorderdetails.component.html',
  styleUrl: './adminorderdetails.component.css',
})
export class AdminorderdetailsComponent implements OnInit{
  private route = inject(ActivatedRoute);
  // private router = inject(Router);
  private adminOrderService = inject(AdminorderService);

  order = signal<Order | null>(null);
  isLoading = signal<boolean>(true);
  isUpdating = signal<boolean>(false);

  constructor(private router: Router) {}

  goBack() {
    this.router.navigate(['/admin/order']);
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadOrder(id);
    }
  }

  loadOrder(id: number): void {
    this.isLoading.set(true);
    this.adminOrderService.getOrderById(id).subscribe({
      next: (data) => {
        this.order.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error("Failed to load order", err);
        this.isLoading.set(false);
      }
    });
  }

  onUpdateStatus(newStatus: string): void {
    const currentOrder = this.order();
    if (!currentOrder) return;

    if (confirm(`Change status to ${newStatus}?`)) {
      this.isUpdating.set(true);
      this.adminOrderService.updateStatus(currentOrder.id, newStatus).subscribe({
        next: (updatedOrder) => {
          this.order.set(updatedOrder);
          this.isUpdating.set(false);
        },
        error: (err) => {
          console.error("Update failed", err);
          this.isUpdating.set(false);
          alert("Could not update status.");
        }
      });
    }
  }
}
