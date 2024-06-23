import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interface/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly baseUrl = 'http://localhost:8080/api/v1/user';
  constructor(private http: HttpClient) { }

  getListUser(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}});
  }

  getUser(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  isAuthenticated(): Observable<any> {
    const header = new HttpHeaders().append('Authorization', `Bearer ${localStorage.getItem('accessToken')}`);
    return this.http.get(`${this.baseUrl}/is-authenticated`, {headers: header});
  }

  uploadFile(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('imageFile', file);
    return this.http.post(`${this.baseUrl}/upload`, formData);
  }

  saveUser(user: User, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('user', new Blob([JSON.stringify(user)], {type: 'application/json'}));
    formData.append('file', file, file.name);

    return this.http.post(this.baseUrl, formData);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
