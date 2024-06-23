import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { CartDB } from 'src/app/interface/cart';
import { ProductDetailSize } from 'src/app/interface/product-detail-size';
import { AccountService } from 'src/app/services/account.service';
import { CartService } from 'src/app/services/cart.service';
import { ProductDetailService } from 'src/app/services/product-detail.service';
import { ReviewService } from 'src/app/services/review.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
})
export class ProductDetailComponent implements OnInit {
  mainImage = '';
  productImage: string[] = [];
  productSize: ProductDetailSize[] = [];

  productDetailId: number = 0;
  productId: number = 0;
  productQuantity: number = -1;
  productName: string = '';
  productDescription: string = '';
  productPrice: number = 0;
  productDetailImage: string = '';
  sizeId: any;
  sizeName: string = '';
  quantity: number = 1;
  userId: any;
  isAuthenticated: boolean = false;

  statusFilterReview: boolean = false;
  statusSortReview: boolean = false;
  reviewList: any;
  sumRatingReview: number = 0;
  countReview: number = 0;
  countReviewPerRating: number = 0;
  rating5Star: number = 0;
  rating4Star: number = 0;
  rating3Star: number = 0;
  rating2Star: number = 0;
  rating1Star: number = 0;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productDetailService: ProductDetailService,
    private cartService: CartService,
    private accountService: AccountService,
    private userService: UserService,
    private reviewService: ReviewService,
    private toast: ToastAlertService,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (number: number) => string
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.productDetailId = +id;
        window.scrollTo(0, 0);
        this.getProductDetailClient(+id);
        this.getAllReview(+id);
        this.countReviewByProductDetail(+id);
        this.countReviewByRating1Star(+id);
        this.countReviewByRating2Star(+id);
        this.countReviewByRating3Star(+id);
        this.countReviewByRating4Star(+id);
        this.countReviewByRating5Star(+id);
      }
    });
    if (localStorage.getItem('accessToken')) {
      this.getUser();
    }
  }

  onAscQty() {
    this.quantity++;
  }

  onDescQty() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  showCart() {
    this.router.navigate(['product-cart'], { relativeTo: this.route });
  }

  getProductDetailClient(productId: number) {
    this.productDetailService
      .getProductDetailClient(productId)
      .subscribe((res: any) => {
        this.productId = res.productId;
        this.productName = res.name;
        this.productQuantity = res.quantity;
        this.productDescription = res.description;
        this.productPrice = res.price;
        this.sizeId = res.sizeId;
        this.sizeName = ' Size ' + res.sizeName;
        this.productDetailImage = res.image;

        this.getProductDetailByProductAndSize(this.productId, this.sizeId);

        res.productDetails.forEach((detail: any) => {
          const size = {
            id: detail.sizeId,
            name: detail.sizeName,
            quantity: detail.quantity,
            price: detail.price,
          };
          this.productSize.push(size);
        });
        // Get mainImage
        this.mainImage = this.getMainImage(res.id, res.image);

        // Get sub images
        for (let i = 0; i < res.productImages.length; i++) {
          this.getProductImage(
            res.id,
            res.productImages[i].id,
            res.productImages[i].name
          );
        }
      });
  }

  getMainImage(productId: number, productImageName: string): string {
    return (
      'assets/image-data/product-image/' + productId + '/' + productImageName
    );
  }

  getProductImage(
    productId: number,
    productImageId: number,
    productImageName: string
  ): string[] {
    this.productImage.push(
      'assets/image-data/product-image/' +
        productId +
        '/' +
        productImageId +
        '/' +
        productImageName
    );
    return this.productImage;
  }

  getUser() {
    this.accountService.getProfileUser().subscribe((res: any) => {
      this.userId = res.userId;
    });
  }

  getProductDetailByProductAndSize(productId: number, sizeId: number) {
    this.productDetailService
      .getProductDetailByProductAndSize(productId, sizeId)
      .subscribe((res: any) => {
        this.productDetailId = res.id;
      });
  }

  addProductInCart() {
    if (this.productQuantity < this.quantity) {
      alert('Bạn không được thêm số lượng quá');
    } else {
      if (localStorage.getItem('accessToken')) {
        const formProductCart: CartDB = {
          userId: this.userId,
          productDetailId: this.productDetailId,
          quantity: this.quantity,
          totalPrice: this.productPrice * this.quantity,
        };
        this.cartService
          .addProductToCartDB(formProductCart)
          .subscribe((res: any) => {
            if (res.status === 'Successfully') {
              alert('Thêm sản phẩm vào giỏ thành công');
            } else {
              alert('Thêm sản phẩm vào giỏ thất bại');
            }
          });
      } else {
        const formProductCart = {
          userId: this.userId,
          productDetailId: this.productDetailId,
          quantity: this.quantity,
          totalPrice: this.productPrice * this.quantity,
          productImage: this.productDetailImage,
          productName: this.productName,
          sizeName: this.sizeName,
        };
        const cartList = sessionStorage.getItem('cart');
        if (cartList) {
          let arr = JSON.parse(cartList);
          let found = false;
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].productDetailId === formProductCart.productDetailId) {
              arr[i].quantity = arr[i].quantity + formProductCart.quantity;
              found = true;
              alert('Thêm sản phẩm vào giỏ thành công');
              break;
            }
          }

          if (!found) {
            arr.push(formProductCart);
            alert('Thêm sản phẩm vào giỏ thành công');
          }

          sessionStorage.setItem('cart', JSON.stringify(arr));
        } else {
          sessionStorage.setItem('cart', JSON.stringify([formProductCart]));
          alert('Thêm sản phẩm vào giỏ thành công');
        }
      }
    }
  }

  getAllReview(idProductDetail: number) {
    this.reviewService
      .findAllReviewByProductDetail(idProductDetail)
      .subscribe((res: any) => {
        this.reviewList = res;
      });
  }

  countReviewByProductDetail(idProductDetail: number) {
    this.reviewService
      .countReviewByProductDetail(idProductDetail)
      .subscribe((res: any) => {
        this.countReview = res;
      });
  }

  countReviewByRating5Star(productDetail: number) {
    this.reviewService
      .countReviewByRatingAndProductDetail(5, productDetail)
      .subscribe((res: any) => {
        this.rating5Star = res;
        this.sumRatingReview += res * 5;
      });
  }
  countReviewByRating4Star(productDetail: number) {
    this.reviewService
      .countReviewByRatingAndProductDetail(4, productDetail)
      .subscribe((res: any) => {
        this.rating4Star = res;
        this.sumRatingReview += res * 4;
      });
  }
  countReviewByRating3Star(productDetail: number) {
    this.reviewService
      .countReviewByRatingAndProductDetail(3, productDetail)
      .subscribe((res: any) => {
        this.rating3Star = res;
        this.sumRatingReview += res * 3;
      });
  }
  countReviewByRating2Star(productDetail: number) {
    this.reviewService
      .countReviewByRatingAndProductDetail(2, productDetail)
      .subscribe((res: any) => {
        this.rating2Star = res;
        this.sumRatingReview += res * 2;
      });
  }
  countReviewByRating1Star(productDetail: number) {
    this.reviewService
      .countReviewByRatingAndProductDetail(1, productDetail)
      .subscribe((res: any) => {
        this.rating1Star = res;
        this.sumRatingReview += res * 1;
      });
  }

  checkAuthenticated() {
    this.userService.isAuthenticated().subscribe((res) => {
      this.isAuthenticated = res;
      console.log(res);
    });
  }

  findQuantityBySize(sizeId: number) {
    const foundSize = this.productSize.find((s) => s.id === sizeId);
    this.productQuantity = foundSize ? foundSize.quantity : -1;
    this.sizeName = foundSize ? ' Size ' + foundSize.name : '';
    this.productPrice = foundSize ? foundSize.price : 0;

    this.getProductDetailByProductAndSize(this.productId, sizeId);
  }

  formatNumber(inputNumber: number): string {
    return this.formatNumberWithCommas(inputNumber);
  }

  formatTimestamp(timestamp: number): string {
    const date = new Date(timestamp);
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    };

    return date.toLocaleString('vi-VN', options);
  }

  repeatRatingArray(rating: number): number[] {
    return new Array(rating);
  }

  onStatusFilterReview() {
    if (this.statusSortReview) {
      this.statusFilterReview = false;
      this.statusSortReview = false;
    } else {
      this.statusFilterReview = false;
      this.statusSortReview = true;
    }
  }

  onStatusSortReview() {
    if (this.statusFilterReview) {
      this.statusFilterReview = false;
      this.statusSortReview = false;
    } else {
      this.statusFilterReview = true;
      this.statusSortReview = false;
    }
  }
}
