import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from '../interface/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  readonly baseUrl = 'http://localhost:8080/api/v1/role';
  constructor(private http: HttpClient) { }

  getListRoles(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getPageRole(pageNum: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/page`, {params: {pageNum: pageNum}})
  }

  getRole(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/ + ${id}`);
  }

  saveRole(role: Role): Observable<any> {
    return this.http.post(this.baseUrl, role);
  }

  deleteRole(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ + ${id}`);
  }
}
