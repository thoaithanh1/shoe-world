import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Size } from '../interface/size';

@Injectable({
  providedIn: 'root'
})
export class SizeService {

  readonly baseUrl = 'http://localhost:8080/api/v1/size';
  constructor(private http: HttpClient) { }

  getListSize(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getPageSize(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}})
  }

  getSize(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveSize(size: Size): Observable<any> {
    return this.http.post(this.baseUrl, size);
  }

  deleteSize(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
