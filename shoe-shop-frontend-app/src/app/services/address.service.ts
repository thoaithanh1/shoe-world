import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  readonly apiAddressVietNam = 'https://provinces.open-api.vn/api/';

  readonly baseUrl = 'http://localhost:8080/api/v1/address';

  constructor(private http: HttpClient) { }

  getListProcince(): Observable<any> {
    return this.http.get(this.apiAddressVietNam);
  }

  getListDistrict(provinceId: number): Observable<any> {
    return this.http.get(`${this.apiAddressVietNam}p/${provinceId}?depth=2`)
  }

  getListWard(districtId: number): Observable<any> {
    return this.http.get(`${this.apiAddressVietNam}d/${districtId}?depth=2`)
  }

  getAddressById(addressId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${addressId}`);
  }

  getAllAddressByUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/user/${userId}`);
  }

  saveAddress(address: any): Observable<any> {
    return this.http.post(this.baseUrl, address);
  }

  deleteAddress(addressId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${addressId}`);
  }
}
