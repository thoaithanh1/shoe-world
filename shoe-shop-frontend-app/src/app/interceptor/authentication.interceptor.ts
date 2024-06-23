import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
  private securesUrl: {url: string; httpMethod: string}[] = [
    {url: '/api/v1/brand', httpMethod: 'POST'},
    {url: '/api/v1/brand/', httpMethod: 'DELETE'},
    {url: '/api/v1/category', httpMethod: 'POST'},
    {url: '/api/v1/category', httpMethod: 'PUT'},
    {url: '/api/v1/category/', httpMethod: 'DELETE'},
    {url: '/api/v1/color', httpMethod: 'POST'},
    {url: '/api/v1/color/', httpMethod: 'DELETE'},
    {url: '/api/v1/size', httpMethod: 'POST'},
    {url: '/api/v1/size/', httpMethod: 'DELETE'},
    {url: '/api/v1/role', httpMethod: 'POST'},
    {url: '/api/v1/role/', httpMethod: 'DELETE'},
    {url: '/api/v1/user', httpMethod: 'POST'},
    {url: '/api/v1/product/save', httpMethod: 'POST'},
    {url: '/api/v1/product/', httpMethod: 'DELETE'},
    {url: '/api/v1/product-detail', httpMethod: 'POST'},
    {url: '/api/v1/product-detail/', httpMethod: 'DELETE'},
    {url: '/api/v1/favorite/', httpMethod: 'GET'},
    {url: '/api/v1/favorite', httpMethod: 'POST'},
    {url: '/api/v1/favorite/', httpMethod: 'DELETE'},
    {url: '/api/v1/payment/', httpMethod: 'GET'},
    {url: '/api/v1/payment/vnpay', httpMethod: 'GET'},
    {url: '/api/v1/payment/by-cash', httpMethod: 'POST'},
    {url: '/api/v1/payment/vnpay/response', httpMethod: 'GET'},
    {url: '/api/v1/address', httpMethod: 'GET'},
    {url: '/api/v1/address', httpMethod: 'POST'},
    {url: '/api/v1/address/', httpMethod: 'DELETE'},
    {url: '/api/v1/order/', httpMethod: 'GET'},
    {url: '/api/v1/order', httpMethod: 'GET'},
    {url: '/api/v1/promotion', httpMethod: 'GET'},
    {url: '/api/v1/promotion/filter', httpMethod: 'GET'},
    {url: '/api/v1/promotion', httpMethod: 'POST'},
    {url: '/api/v1/review', httpMethod: 'POST'},
  ];
  constructor() {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if(this.isSecureRequest(request.url, request.method)){
      // console.log("Url", request.url);
      // console.log("Method", request.method);
      
      if (localStorage.getItem('accessToken') != null || localStorage.getItem('accessToken') != '') {
        request = request.clone({
          setHeaders: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
          },
        });
      }
    }
    return next.handle(request);
  }

  private isSecureRequest(url: string, httpMethod: string): boolean {
    return this.securesUrl.some(
      secureUrl => url.includes(secureUrl.url) && httpMethod == secureUrl.httpMethod
    )
  }
}