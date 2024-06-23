import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from 'src/app/services/cart.service';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  qty: number = 1;
  listCart: any;
  listCartNotLoggedIn: any;
  userId: number = 0;
  listCartHasData: boolean = false;
  listCartHasLocal: boolean = false;
  totalCart: number = 0;
  isEmptyCart: boolean = false;
  countProductCart = 0;

  constructor(
    private cartService: CartService,
    private router: Router,
    private route: ActivatedRoute,
    private toast: ToastAlertService,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (number: number) => string
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const userId = params.get('userId');
      if (userId) {
        if (+userId === 0) {
          const cart = sessionStorage.getItem('cart');
          this.listCartNotLoggedIn = JSON.parse(cart!);
          for (let i = 0; i < this.listCartNotLoggedIn.length; i++) {
            this.totalCart +=
              this.listCartNotLoggedIn[i].quantity *
              this.listCartNotLoggedIn[i].totalPrice;
              this.countProductCart = 1;
          }
          this.listCartHasLocal =
            Array.isArray(this.listCartNotLoggedIn) &&
            this.listCartNotLoggedIn.length > 0;
        } else {
          this.userId = +userId;
          this.getListCartByUser(+userId);
          this.getCartByUser(+userId);
        }
      }
    });

    this.isEmptyCart = this.listCartHasData == this.listCartHasLocal;
  }

  onAscQty(productDetailId: number, quantity: number, price: number) {
    if (localStorage.getItem('accessToken')) {
      const cart = {
        userId: this.userId,
        productDetailId: productDetailId,
        quantity: quantity + 1,
        totalPrice: this.totalCart + price,
      };
      this.cartService.updateProductQtyInCartByUser(cart).subscribe((data) => {
        if (data.status === 'Successfully') {
          this.getListCartByUser(this.userId);
          this.getCartByUser(this.userId);
        }
      });
    }

    if (sessionStorage.getItem('cart')) {
      const cartList = sessionStorage.getItem('cart');
      if (cartList) {
        let arr = JSON.parse(cartList);
        console.log(arr);
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].productDetailId === productDetailId) {
            arr[i].quantity = arr[i].quantity + 1;
          }
          this.totalCart = 0;
          this.totalCart += arr[i].quantity * arr[i].totalPrice;
        }

        sessionStorage.setItem('cart', JSON.stringify(arr));
        this.listCartNotLoggedIn = arr;
      }
    }
  }

  onDescQty(productDetailId: number, quantity: number, price: number) {
    if (localStorage.getItem('accessToken')) {
      if (quantity > 1) {
        const cart = {
          userId: this.userId,
          productDetailId: productDetailId,
          quantity: quantity - 1,
          totalPrice: this.totalCart - price,
        };
        this.cartService
          .updateProductQtyInCartByUser(cart)
          .subscribe((data) => {
            if (data.status === 'Successfully') {
              this.getListCartByUser(this.userId);
              this.getCartByUser(this.userId);
            }
          });
      }
    }

    if (sessionStorage.getItem('cart')) {
      const cartList = sessionStorage.getItem('cart');
      if (cartList) {
        let arr = JSON.parse(cartList);
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].productDetailId === productDetailId) {
            if (arr[i].quantity > 1) {
              arr[i].quantity = arr[i].quantity - 1;
            }
          }
          this.totalCart = 0;
          this.totalCart += arr[i].quantity * arr[i].totalPrice;
        }
        sessionStorage.setItem('cart', JSON.stringify(arr));
        this.listCartNotLoggedIn = arr;
      }
    }
  }

  getListCartByUser(userId: number) {
    this.cartService.getCartDB(userId).subscribe((res: any) => {
      this.listCart = res;
      this.listCartHasData = this.listCart.length > 0;
      this.countProductCart = 1;
    });
  }

  getCartByUser(userId: number) {
    this.cartService.getCartByUser(userId).subscribe((res: any) => {
      this.totalCart = res.total;
    });
  }

  getProductImage(productId: number, productImage: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productImage;
  }

  deleteProductInCart(productDetailId: number) {
    this.cartService.deleteProductInCart(productDetailId).subscribe((res) => {
      if (res === 0) {
        this.toast.showAlertSuccess('Thành công', 'Đã xóa thành công sản phẩm');
        window.location.reload();
      } else {
        this.toast.showAlertFailure(
          'Lỗi',
          'Có lỗi xảy ra trong quá trình xóa '
        );
        window.location.reload();
      }
    });
  }

  deleteProductInCartSession(productDetailId: number) {
    if (sessionStorage.getItem('cart')) {
      const cartSession = JSON.parse(sessionStorage.getItem('cart')!);
      if (cartSession) {
        for (let i = 0; i < cartSession.length; i++) {
          if (cartSession[i].productDetailId === productDetailId) {
            this.totalCart =
              this.totalCart -
              cartSession[i].quantity * cartSession[i].totalPrice;
            cartSession.splice(i, 1);
            alert('Xóa thành công sản phẩm khỏi giỏ hàng');
            this.isEmptyCart = true;
            break;
          }
        }
        sessionStorage.setItem('cart', JSON.stringify(cartSession));
        this.listCartNotLoggedIn = JSON.parse(sessionStorage.getItem('cart')!);
        this.listCartNotLoggedIn.length > 0 ? this.countProductCart = 1 : this.countProductCart = 0;
      }
    }
  }

  redirectToPayment() {
    if (this.countProductCart != 0) {
      this.router.navigate(['/checkout/' + this.userId]);
    } else {
      alert('Khong co san pham nao trong gio hang');
    }
  }

  formatNumber(number: number): string {
    return this.formatNumberWithCommas(number);
  }
}
