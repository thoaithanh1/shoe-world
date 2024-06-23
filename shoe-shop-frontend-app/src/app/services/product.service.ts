import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../interface/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  readonly baseUrl = 'http://localhost:8080/api/v1/product';
  constructor(private http: HttpClient) {}

  getListProduct(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getListProductByPage(page: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, { params: { pageNum: page } });
  }

  getPageProductDisplayHome(page: number, brandId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/show/page`, { params: { pageNum: page, brandId: brandId } });
  }

  filterProduct(productFilter: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/filter`, productFilter);
  }

  getProduct(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveProduct(product: Product): Observable<any> {
    return this.http.post(`${this.baseUrl}/save`, product);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
