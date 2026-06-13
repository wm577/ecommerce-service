import {Component, inject, signal} from '@angular/core';
import {Product} from '../../../../shared/models/product.model';
import {ProductlistService} from '../../services/productlist.service';
import {CurrencyPipe, NgForOf, NgIf} from '@angular/common';
import {CartService} from '../../services/cart.service';

@Component({
  selector: 'app-productlist',
  imports: [
    CurrencyPipe,
    NgForOf,
    NgIf,
  ],
  templateUrl: './productlist.component.html',
  styleUrl: './productlist.component.css',
})
export class ProductlistComponent {
  productList= signal<Product[]>([]);
  private productListService = inject(ProductlistService);
  private cartService = inject(CartService);
  userId: number = 1;

  constructor() {
  }

  ngOnInit(): void {
    this.loadAllProducts();
  }

  addToCart(productId: number) {
    this.cartService.addProductToCart(this.userId, productId).subscribe({
      next: (message) => {
        console.log(message);
        this.refreshCartCount(this.userId);
        alert("Product added to your cart successfully!");
      },
      error: (err) => {
        console.error(err);
        alert("Could not add product to cart. Maybe it's out of stock?");
      }
    });
  }

  private refreshCartCount(userId: number) {
    this.cartService.getCartItemsByUserId(userId).subscribe({
      next: (cart) => {
        this.cartService.cartCount$.next(cart.cartItems.length);
      },
      error: (err) => console.error("Error updating cart count", err)
    });
  }

  loadAllProducts() {
    this.productListService.getAllProductsForCustomer().subscribe({
      next: (data) => this.productList.set(data),
      error: (err) => console.error("Load failed", err)
    });
  }

  getProductByName(name: string) {
    if (!name || name.trim() === '') {
      this.loadAllProducts();
      return;
    }

    this.productListService.searchProductByName(name).subscribe({
      next: (data) => this.productList.set(data),
      error: (err) => {
        console.error("Search failed", err);
        this.productList.set([]); // Clear results if nothing found
      }
    });
  }

  resetSearch() {
    this.loadAllProducts();
  }
}
