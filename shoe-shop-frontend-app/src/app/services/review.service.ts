import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  readonly baseUrl = 'http://localhost:8080/api/v1/review';
  constructor(private http: HttpClient) { }

  findAllReviewByProductDetail(idProductDetail: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${idProductDetail}`);
  }

  countReviewByProductDetail(idProductDetail: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/count/${idProductDetail}`);
  }

  countReviewByRatingAndProductDetail(rating: number, idProductDetail: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/count-per-rating/${rating}/${idProductDetail}`);
  }

  checkProductReviewed(orderId: number, productDetailId: number, userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/check-reviewed/${orderId}/${productDetailId}/${userId}`);
  }

  saveReview(review: any, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('review', new Blob([JSON.stringify(review)], {type: 'application/json'}));
    formData.append('file', file, file.name);

    return this.http.post(this.baseUrl, formData);
  }
}
