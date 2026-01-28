import {Component, inject, OnInit, signal} from '@angular/core';
import {CartService} from '../../services/cart.service';
import {CartItemDto} from '../../../../shared/models/cart.model';
import {CommonModule, CurrencyPipe, NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgModel} from '@angular/forms';
import {switchMap} from 'rxjs';

@Component({
  selector: 'app-cartitems',
  imports: [
    CurrencyPipe,
    FormsModule,
    CommonModule
  ],
  templateUrl: './cartitems.component.html',
  styleUrl: './cartitems.component.css',
})
export class CartitemsComponent implements OnInit{
  cartItemList= signal<CartItemDto[]>([]);
  private cartService = inject(CartService);
  userId: number = 1;
  couponInput: string = "";
  totalAmount = signal<number>(0);
  private router = inject(Router);

  constructor() {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.cartService.getCartItemsByUserId(this.userId)
      .subscribe({next: (data) => {
          this.cartItemList.set(data.cartItems);
          this.totalAmount.set(data.totalAmount);
        },
        error: (err) => console.error("Load failed", err)
      });
  }

  remove(cartItemId: number) {
    this.cartService.removeCartItem(this.userId, cartItemId).subscribe({
      next: (response) => {
        console.log(response);
        this.loadCart();
        this.refreshNavbarCount();
      },
      error: (err) => console.error("Remove failed", err)
    });
  }

  private refreshNavbarCount() {
    this.cartService.getCartItemsByUserId(this.userId).subscribe(cart => {
      this.cartService.cartCount$.next(cart.cartItems.length);
    });
  }

  goToProductList(){
    this.router.navigate(['/customer/products']);
  }

  onPlaceOrder() {
    this.cartService.checkOutCartItem(this.userId, this.couponInput).pipe(
      switchMap((res) => {
        console.log('Order created successfully!', res);
        return this.cartService.clearCart(this.userId);
      })
    ).subscribe({
      next: () => {
        this.resetLocalCart();
        alert('Order placed successfully! Your cart is cleared.');
      },
      error: (err) => {
        console.error('Process failed', err);
        if (err.status === 404) {
          this.resetLocalCart();
          alert('Order placed! (Note: Local cart cleared, but server sync had an issue).');
        }
      }
    });
  }

  private resetLocalCart() {
    this.cartItemList.set([]);
    this.totalAmount.set(0);
    this.cartService.cartCount$.next(0);
  }

  deleteAllItems() {
    if(confirm("Are you sure you want to empty your cart?")) {
      this.cartService.clearCart(this.userId).subscribe({
        next: (msg) => {
          this.cartItemList.set([]);
          this.totalAmount.set(0);
          this.refreshNavbarCount();
        }
      });
    }
  }
}
