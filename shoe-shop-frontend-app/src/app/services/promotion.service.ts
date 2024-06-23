import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PromotionService {
  readonly baseUrl = 'http://localhost:8080/api/v1/promotion';
  constructor(private http: HttpClient) {}

  getListPromotion(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  findPromotion(promotionId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${promotionId}`);
  }

  filterPromotion(
    keyword: string,
    status: string,
    method: string,
    operator: string
  ): Observable<any> {
    return this.http.get(`${this.baseUrl}/filter`, {
      params: { keyword: keyword, status: status, method: method, operator: operator },
    });
  }

  findPromotionByCondition(conditionApply: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/get-promotion-apply/${conditionApply}`);
  }

  savePromotion(promotion: any): Observable<any> {
    return this.http.post(`${this.baseUrl}`, promotion);
  }
}
