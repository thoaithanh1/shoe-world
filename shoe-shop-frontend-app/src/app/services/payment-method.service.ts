import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentMethodService {
  readonly baseUrl = 'http://localhost:8080/api/v1/payment-method';

  constructor(private http: HttpClient) { }

  getListPaymentMethod(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}
