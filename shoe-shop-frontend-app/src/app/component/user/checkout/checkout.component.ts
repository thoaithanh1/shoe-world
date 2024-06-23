import {
  Component,
  ElementRef,
  Inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { timeout } from 'rxjs';
import { AddressService } from 'src/app/services/address.service';
import { CartService } from 'src/app/services/cart.service';
import { PaymentMethodService } from 'src/app/services/payment-method.service';
import { PaymentService } from 'src/app/services/payment.service';
import { PromotionService } from 'src/app/services/promotion.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
})
export class CheckoutComponent implements OnInit {
  listAddress: any;
  cart: any;
  listCartDetail: any;
  listPaymentMethod: any;
  shippingFee: number = 30000;
  @ViewChild('note', { static: false }) note!: ElementRef;
  paymentMethodId: number = 1;
  userId: number = 0;
  order: any;
  formAddress: any;
  switchAddress: boolean = true;
  titleAddress: string = 'Địa chỉ của tôi';
  promotion = {
    discountValue: 0,
    promotionId: null,
  };
  totalPriceCart: number = 0;

  constructor(
    private paymentService: PaymentService,
    private paymentMethodService: PaymentMethodService,
    private addressService: AddressService,
    private promotionService: PromotionService,
    private cartService: CartService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (number: number) => string
  ) {
    this.formAddress = this.fb.group({
      id: '',
      city: ['', [Validators.required]],
      district: ['', [Validators.required]],
      wards: ['', [Validators.required]],
      location: ['', [Validators.required]],
      isDefault: [false, [Validators.required]],
      user: this.fb.group({
        id: '',
      }),
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: any) => {
      const userId = params.get('userId');
      if (userId) {
        this.userId = +userId;
        this.showCartInPayment(+userId);
        this.totalPriceCartInUser(+userId);
      }
    });
    this.getListPaymentMethod();
    console.log(this.promotion);
  }

  showCartInPayment(userId: number) {
    this.paymentService.showCartInPayment(userId).subscribe((res: any) => {
      this.listAddress = res.addresses;
      this.cart = res.carts;
      this.listCartDetail = res.cartDetails;
    });
  }

  getProductImage(productId: number, productImage: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productImage;
  }

  selectPaymentMethod(paymentMethodId: number, type: number) {
    if (type === 0) {
      this.shippingFee = 30000;
    } else if (type === 1) {
      this.shippingFee = 0;
    }
    this.paymentMethodId = paymentMethodId;
  }

  onPaymentOrder() {
    if (this.shippingFee > 0) {
      const confirm = window.confirm(
        'Bạn có chắc chắn muốn đặt đơn hàng này bằng phương thức thanh toán khi nhận hàng'
      );
      if (confirm) {
        const getOrder = (this.order = {
          shippingFee: this.shippingFee,
          totalPrice: this.cart.total,
          note: this.note.nativeElement.value,
          paymentMethodId: this.paymentMethodId,
          userId: this.userId,
          promotionId: this.promotion.promotionId,
          cartDetailDtos: this.listCartDetail,
        });
        console.log(getOrder);
        this.paymentService
          .paymentOrderByPayAfterReceive(getOrder)
          .subscribe((res: any) => {
            if (res.status == 1) {
              this.router.navigate(['/api/v1/payment/cash/success']);
            } else {
              this.router.navigate(['/api/v1/payment/cash/fail']);
            }
          });
      }
    } else {
      const confirm = window.confirm(
        'Bạn có chắc chắn muốn đặt đơn hàng này bằng phương thức thanh toán VnPay'
      );
      if (confirm) {
        const totalPrice = this.shippingFee + this.cart.total;
        const note =
          this.note.nativeElement.value === ''
            ? ' '
            : this.note.nativeElement.value;
        this.paymentService
          .paymentOrderByVnPay(totalPrice, note)
          .subscribe((res: any) => {
            window.location.href = res.url;
          });
      }
    }
  }

  getListPaymentMethod() {
    this.paymentMethodService.getListPaymentMethod().subscribe((res: any) => {
      this.listPaymentMethod = res;
    });
  }

  formatPrice(price: number) {
    return this.formatNumberWithCommas(price);
  }

  getAddressById(addressId: number) {
    this.addressService.getAddressById(addressId).subscribe((res: any) => {
      this.formAddress.patchValue(res);
      // console.log(res);
    });
  }

  totalPriceCartInUser(userId: number) {
    this.cartService.totalPriceInCartByUser(userId).subscribe((res: any) => {
      this.totalPriceCart = res;
      this.checkPromotionApply(res);
    });
  }

  checkPromotionApply(conditionApply: number) {
    this.promotionService
      .findPromotionByCondition(conditionApply)
      .subscribe((res: any) => {
        this.promotion = {
          discountValue: res.discountValue,
          promotionId: res.id,
        };
        console.log(res);
      });
  }

  onSwitchAddress(addressId: number) {
    this.getAddressById(addressId);
    this.formAddress.get('user.id').patchValue(this.userId);
    if (this.switchAddress === false) {
      this.formAddress.reset();
    }
    this.switchAddress = this.switchAddress ? false : true;
    this.titleAddress =
      this.titleAddress === 'Địa chỉ của tôi'
        ? 'Địa Chỉ Mới'
        : 'Địa chỉ của tôi';
  }

  onAddress() {
    if (this.formAddress.valid) {
      console.log(this.formAddress.value);

      this.addressService
        .saveAddress(this.formAddress.value)
        .subscribe((res: any) => {
          console.log(res);
          window.location.reload();
        });
    } else {
      alert('Vui lòng nhập đầy đủ thông tin trên');
    }
  }

  onChangeStatus(addressId: number) {
    const addressData = this.getAddressById(addressId);
    this.formAddress.patchValue(addressData);
  }

  onUpdateStatus() {
    if (this.formAddress.get('isDefault').value == true) {
      alert('Địa chỉ đang chọn đã được cài làm địa chỉ mặc định rồi');
    } else if (this.formAddress.invalid) {
      alert('Vui lòng chọn địa chỉ bạn muốn đặt làm mặc định');
    } else {
      this.formAddress.get('isDefault').patchValue(true);
      this.addressService
        .saveAddress(this.formAddress.value)
        .subscribe((res: any) => {
          console.log(res);
          window.location.reload();
        });
    }
  }
}
