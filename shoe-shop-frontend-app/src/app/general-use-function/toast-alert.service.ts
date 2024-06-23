import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToastAlertService {

  constructor(private toast: ToastrService) { }

  showAlertSuccess(title: string, message: string) {
    this.toast.success(message, title, {timeOut: 3000, progressBar: true, closeButton: true});
  }

  showAlertFailure(title: string, message: string) {
    this.toast.error(message, title, {timeOut: 3000, progressBar: true, closeButton: true});
  }

  showAlertInfo(title: string, message: string) {
    this.toast.info(message, title, {timeOut: 3000, progressBar: true, closeButton: true});
  }

  showAlertWarning(title: string, message: string) {
    this.toast.warning(message, title, {timeOut: 3000, progressBar: true, closeButton: true});
  }
}
