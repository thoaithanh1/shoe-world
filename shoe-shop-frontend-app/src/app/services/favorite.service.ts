import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FavoriteService {
  readonly baseUrl = 'http://localhost:8080/api/v1/favorite';
  constructor(private http: HttpClient) {}

  getPageFavoriteByUser(userId: number, pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${userId}`, {
      params: { pageNum: pageNum },
    });
  }

  getCountProductInFavorite(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/count/${userId}`);
  }

  saveFavoriteByUser(favorite: any): Observable<any> {
    return this.http.post(`${this.baseUrl}`, favorite);
  }

  deleteProductInFavoriteByUser(
    userId: number,
    productDetailId: number
  ): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${userId}/${productDetailId}`);
  }
}
