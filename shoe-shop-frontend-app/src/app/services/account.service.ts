import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Login } from '../interface/login';
import { Register } from '../interface/register';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  readonly urlAccount: string = 'http://localhost:8080/api/v1/account';

  constructor(private http: HttpClient) { }

  onLogin(formLogin: Login): Observable<any> {
    return this.http.post(this.urlAccount + "/login", formLogin);
  }

  onRegister(formRegister: Register): Observable<any> {
    return this.http.post(this.urlAccount + "/register", formRegister);
  }

  getProfileUser(): Observable<any> {
    const header = new HttpHeaders().append('Authorization', `Bearer ${localStorage.getItem('accessToken')}`);
    return this.http.get(`${this.urlAccount}/profile`, {headers: header});
  }
}
