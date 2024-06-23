import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  readonly baseUrl = 'http://localhost:8080/api/v1/cart';

  constructor(private http: HttpClient) {}

  getCartByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/user/${userId}`);
  }

  countProductInCartByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/count/${userId}`);
  }

  totalPriceInCartByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/total/${userId}`);
  }

  getCartDB(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${userId}`);
  }

  addProductToCartDB(product: any): Observable<any> {
    return this.http.post(`${this.baseUrl}`, product);
  }

  updateProductQtyInCartByUser(cart: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/update-qty`, cart);
  }

  deleteProductInCart(productDetailId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${productDetailId}`);
  }
}
