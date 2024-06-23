import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductDetailService {

  readonly baseUrl = 'http://localhost:8080/api/v1/product-detail';
  constructor(private http: HttpClient) { }

  getListProductDetail(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getListProductDetailByPage(page: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: page}});
  }

  getProductDetail(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  getProductDetailByProductAndSize(productId: number, sizeId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/detail-cart/${productId}/${sizeId}`);
  }

  getProductDetailClient(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/detail/${id}`);
  }

  saveProductDetail(formData: FormData): Observable<any> {
    return this.http.post(this.baseUrl, formData);
  }

  deleteProductDetail(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
