import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  readonly urlBase = 'http://localhost:8080/api/v1/payment';

  constructor(private http: HttpClient) {}

  showCartInPayment(userId: number): Observable<any> {
    return this.http.get(`${this.urlBase}/${userId}`);
  }

  paymentOrderByPayAfterReceive(order: any): Observable<any> {
    return this.http.post(`${this.urlBase}/by-cash`, order);
  }

  paymentOrderByVnPay(totalPrice: number, note: string): Observable<any> {
    return this.http.get(`${this.urlBase}/vnpay`, {
      params: { totalPrice: totalPrice, note: note },
    });
  }

  getResponseInvoiceAfterPaymentVnpay(
    totalPrice: string,
    paymentType: string,
    codeOrder: string,
    note: string,
    userId: number
  ): Observable<any> {
    return this.http.get(`${this.urlBase}/vnpay/response`, {
      params: {
        vnp_Amount: totalPrice,
        vnp_CardType: paymentType,
        vnp_TxnRef: codeOrder,
        vnp_OrderInfo: note,
        user_id: userId
      },
    });
  }
}
