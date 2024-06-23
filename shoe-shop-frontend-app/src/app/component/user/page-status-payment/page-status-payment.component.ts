import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { AccountService } from 'src/app/services/account.service';
import { PaymentService } from 'src/app/services/payment.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-page-status-payment',
  templateUrl: './page-status-payment.component.html',
  styleUrls: ['./page-status-payment.component.css'],
})
export class PageStatusPaymentComponent implements OnInit {
  totalPrice: string = '';
  paymentType: string = '';
  codeOrder: string = '';
  responseCode: string = '';
  userId: number = 0;

  isAuthenticated: boolean = false;
  userAvatar: string = 'assets/images/default.png';
  countProductInCart: number = 0;
  countProductInFavorite: number = 0;
  totalPriceCart: number = 0;
  note: string = '';

  constructor(
    private route: ActivatedRoute,
    private accountService: AccountService,
    private userService: UserService,
    private paymentService: PaymentService,
    private toast: ToastAlertService
  ) {}

  async ngOnInit() {
    await this.getUser();
    this.route.queryParams.subscribe((params) => {
      this.totalPrice = params['vnp_Amount'];
      this.paymentType = params['vnp_CardType'];
      this.codeOrder = params['vnp_TxnRef'];
      this.responseCode = params['vnp_ResponseCode'];
      this.note = params['vnp_OrderInfo'];

      console.log(this.totalPrice);
      console.log(this.paymentType);
      console.log(this.codeOrder);
      console.log(this.userId);

      this.getResponseAfterPaymentVnpay(
        this.totalPrice,
        this.paymentType,
        this.codeOrder,
        this.note,
        this.userId
      );
    });
  }

  async getUser() {
    const res: any = await this.accountService.getProfileUser().toPromise();
    this.userId = res.userId;
  }

  getResponseAfterPaymentVnpay(
    totalPrice: string,
    paymentType: string,
    codeOrder: string,
    note: string,
    userId: number
  ) {
    if (this.responseCode === '00') {
      this.paymentService
        .getResponseInvoiceAfterPaymentVnpay(
          totalPrice,
          paymentType,
          codeOrder,
          note,
          userId
        )
        .subscribe((res: any) => {
          if (res.status) {
            this.toast.showAlertSuccess('Thành công', 'Đặt hàng thành công!');
          } else {
            this.toast.showAlertFailure('Thất bại', 'Đặt hàng thất bại!');
          }
        });
    } else if (this.responseCode === '24') {
      this.toast.showAlertFailure(
        'Thất bại',
        'Giao dịch thất bại do bạn đã hủy thanh toán.'
      );
    }
  }

  checkAuthenticated() {
    this.userService.isAuthenticated().subscribe((res) => {
      this.isAuthenticated = res;
    });
  }
}
