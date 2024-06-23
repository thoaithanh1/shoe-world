import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Color } from '../interface/color';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  readonly baseUrl = 'http://localhost:8080/api/v1/color';
  constructor(private http: HttpClient) { }

  getListColor(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getPageColor(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}})
  }

  getColor(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveColor(color: Color): Observable<any> {
    return this.http.post(this.baseUrl, color);
  }

  deleteColor(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
