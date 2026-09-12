import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Cart {

  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient) {}

  getCart(userId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/${userId}`);
  }

  addToCart(userId: number, productId: number, quantity: number) {
    return this.http.post<any>(
      `${this.apiUrl}/${userId}/add?productId=${productId}&quantity=${quantity}`,
      {}
    );
  }

  updateQuantity(
    userId: number,
    cartItemId: number,
    quantity: number
  ) {
    return this.http.put<any>(
      `${this.apiUrl}/${userId}/${cartItemId}?quantity=${quantity}`,
      {}
    );
  }

  removeFromCart(userId: number, cartItemId: number) {
    return this.http.delete(
      `${this.apiUrl}/${userId}/${cartItemId}`
    );
  }

  clearCart(userId: number) {
    return this.http.delete(
      `${this.apiUrl}/${userId}/clear`
    );
  }
}