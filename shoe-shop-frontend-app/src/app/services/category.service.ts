import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  readonly baseUrl = 'http://localhost:8080/api/v1/category';
  constructor(private http: HttpClient) { }

  getListCategory(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getPageCategory(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}})
  }

  getCategory(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveCategory(category: any): Observable<any> {
    return this.http.post(this.baseUrl, category);
  }

  deleteCategory(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
