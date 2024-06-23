import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Brand } from '../interface/brand';

@Injectable({
  providedIn: 'root',
})
export class BrandService {
  readonly baseUrl = 'http://localhost:8080/api/v1/brand';
  constructor(private http: HttpClient) {}

  getListBrand(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getPageBrand(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}})
  }

  getBrand(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveBrand(brand: Brand, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append(
      'brand',
      new Blob([JSON.stringify(brand)], { type: 'application/json' })
    );
    formData.append('brandImage', file, file.name);

    return this.http.post(this.baseUrl, formData);
  }

  deleteBrand(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
