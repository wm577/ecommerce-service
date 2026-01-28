import {ChangeDetectorRef, Component, inject, OnInit, PLATFORM_ID} from '@angular/core';
import {CustomerorderService} from '../../services/customerorder.service';
import {Order} from '../../../../shared/models/order.model';
import {CommonModule, DatePipe, isPlatformBrowser} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';

@Component({
  selector: 'app-customerorderdetails',
  imports: [
    DatePipe,
    CommonModule,
    RouterLink
  ],
  templateUrl: './customerorderdetails.component.html',
  styleUrl: './customerorderdetails.component.css',
})
export class CustomerorderdetailsComponent implements OnInit{
  private platformId = inject(PLATFORM_ID);
  public route = inject(ActivatedRoute);
  private customerorderService = inject(CustomerorderService);
  private cdr = inject(ChangeDetectorRef);

  order?: Order;
  errorMessage: string = '';
  isLoading: boolean = true;

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadOrder();
    }
  }

  private loadOrder(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const orderId = idParam ? Number(idParam) : null;

    if (orderId) {
      this.isLoading = true;
      this.customerorderService.getOrderById(orderId).subscribe({
        next: (data) => {
          console.log('Component received data:', data);
          this.order = data;
          this.isLoading = false;
          this.errorMessage = '';
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Subscription error:', err);
          this.errorMessage = 'Could not load order details.';
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.errorMessage = 'Invalid Order ID';
      this.isLoading = false;
    }
  }
}
