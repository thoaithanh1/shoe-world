import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  readonly baseUrl = 'http://localhost:8080/api/v1/order';

  constructor(private http: HttpClient) {}

  getAllOrder(pageNum: number, itemPerPage: number): Observable<any> {
    return this.http.get(`${this.baseUrl}`, {
      params: { pageNum: pageNum, itemPerPage: itemPerPage },
    });
  }

  getOrderByStatus(pageNum: number, itemPerPage: number, status: number, keyword: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/status/${status}`, {
      params: { pageNum: pageNum, itemPerPage: itemPerPage, keyword: keyword },
    });
  }

  getOrderByDate(pageNum: number, itemPerPage: number, date: number, status: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/date/${date}`, {
      params: { pageNum: pageNum, itemPerPage: itemPerPage, status: status },
    });
  }

  getOrderById(orderId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${orderId}`);
  }

  getProductByOrder(orderId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/order-detail/${orderId}`);
  }

  getAllOrderByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/user/${userId}`);
  }

  getAllOrderByUserAndStatus(userId: number, status: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${userId}/${status}`);
  }

  updateStatusOrder(orderId: number, status: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/update-status/${orderId}/${status}`);
  }
}
