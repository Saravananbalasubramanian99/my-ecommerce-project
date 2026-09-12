import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Cart } from '../services/cart';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.html',
  styleUrl: './cart.css'
})
export class CartComponent implements OnInit {

  cartItems: any[] = [];
  total = 0;

  constructor(private cartService: Cart, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    const userId = Number(localStorage.getItem('userId'));

    this.cartService.getCart(userId).subscribe({
        next: (data) => {
        this.cartItems = data;
        this.calculateTotal();

        this.cdr.detectChanges();
        },
        error: (error) => {
        console.error('Error loading cart:', error);
        }
    });
    }

  increaseQuantity(item: any): void {
    const userId = Number(localStorage.getItem('userId'));

    this.cartService
      .updateQuantity(userId, item.id, item.quantity + 1)
      .subscribe(() => {
        this.loadCart();
      });
  }

  decreaseQuantity(item: any): void {

    if (item.quantity <= 1) {
      return;
    }

    const userId = Number(localStorage.getItem('userId'));

    this.cartService
      .updateQuantity(userId, item.id, item.quantity - 1)
      .subscribe(() => {
        this.loadCart();
      });
  }

  removeItem(item: any): void {
    const userId = Number(localStorage.getItem('userId'));

    this.cartService
      .removeFromCart(userId, item.id)
      .subscribe(() => {
        this.loadCart();
      });
  }

  clearCart(): void {
    const userId = Number(localStorage.getItem('userId'));

    this.cartService
      .clearCart(userId)
      .subscribe(() => {
        this.loadCart();
      });
  }

  calculateTotal(): void {
    this.total = this.cartItems.reduce(
      (sum, item) =>
        sum + (item.product.price * item.quantity),
      0
    );
  }
}