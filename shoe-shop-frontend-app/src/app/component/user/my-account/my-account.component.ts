import { Component, ElementRef, HostListener, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { timeout } from 'rxjs';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { AddressService } from 'src/app/services/address.service';
import { FavoriteService } from 'src/app/services/favorite.service';
import { OrderService } from 'src/app/services/order.service';
import { ReviewService } from 'src/app/services/review.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent implements OnInit {
  isHidden: boolean = true;
  scrollY: number = 0;

  selectedMenuItem: string = 'order';
  listAddress: any;
  listProvince: any;
  listDistrict: any;
  listWard: any;
  selectedProvinceId: number | undefined;
  selectedDistrictId: number | undefined;
  formAddress: any;
  formUser: any;
  username: any;
  userImage: string = '';
  selectedImage: string | ArrayBuffer | null = 'assets/images/default-user.png';
  selectedFile: File | null = null;
  order: any;
  userId: number = 0;
  listFavorite: any;
  ratingStar: number = 5;
  formReview: any;
  productReview = {
    productDetailId: 0,
    name: '',
    image: '',
    color: '',
    size: '',
  };
  @ViewChild('clickClose') private clickClose!: ElementRef;

  constructor(
    private addressService: AddressService,
    private userService: UserService,
    private orderService: OrderService,
    private favoriteService: FavoriteService,
    private reviewService: ReviewService,
    private toast: ToastAlertService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    @Inject('scrollToTopSmoothly')
    private scrollToTopSmoothly: (end: number) => void,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (price: number) => string
  ) {
    this.formAddress = this.fb.group({
      location: '',
    });
    this.formUser = this.fb.group({
      id: '',
      avatar: '',
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: '',
      phoneNumber: ['', Validators.required],
      gender: [+'', Validators.required],
      dob: ['', Validators.required],
    });
    this.formReview = this.fb.group({
      comment: ['', [Validators.required, Validators.minLength(20)]],
      rating: this.ratingStar,
      user: this.fb.group({
        id: ['', Validators.required],
      }),
      productDetail: this.fb.group({
        id: ['', Validators.required],
      }),
      order: this.fb.group({
        id: ['', Validators.required],
      }),
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const userId = params.get('userId');
      if (userId) {
        this.userId = +userId;
        this.getAllAddressByUser(+userId);
        this.getUserById(+userId);
        this.getAllOrderByUser(+userId);
      }
    });
    this.getListProvince();
    this.getFavoriteByUser(0);

    this.checkProductReviewed(1, 498, 1);
  }

  get comment() {
    return this.formReview.get('comment');
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    this.scrollY = window.scrollY;
  }

  // Address
  getListProvince() {
    this.addressService.getListProcince().subscribe((res: any) => {
      this.listProvince = res;
    });
  }

  getListDistrictByProvince(event: any) {
    if (event && event.target) {
      this.selectedProvinceId = event.target.value;
      if (this.selectedProvinceId) {
        this.addressService
          .getListDistrict(this.selectedProvinceId)
          .subscribe((res: any) => {
            this.listDistrict = res.districts;
          });
      }
    }
  }

  getListWardByDistrict(event: any) {
    if (event && event.target) {
      this.selectedDistrictId = event.target.value;
      if (this.selectedDistrictId) {
        this.addressService
          .getListWard(this.selectedDistrictId)
          .subscribe((res: any) => {
            this.listWard = res.wards;
          });
      }
    }
  }

  getAllAddressByUser(userId: number) {
    this.addressService.getAllAddressByUser(userId).subscribe((res: any) => {
      this.listAddress = res;
    });
  }

  getAddressById(addressId: number) {
    this.addressService.getAddressById(addressId).subscribe((res: any) => {
      this.formAddress.patchValue(res);
    });
  }

  deleteAddress(addressId: number, userId: number) {
    const noticeConfirm = confirm(
      'Bạn có chắc chắn muốn xóa địa chỉ này không?'
    );
    if (noticeConfirm) {
      this.addressService.deleteAddress(addressId).subscribe(() => {
        this.getAllAddressByUser(userId);
        alert('Đã xóa thành công!');
      });
    }
  }

  //User
  getUserById(userId: number) {
    this.userService.getUser(userId).subscribe((res: any) => {
      this.username = res.lastName + ' ' + res.firstName;
      this.getImageUser(res.id, res.avatar);
      res.avatar = null;
      this.formUser.patchValue(res);
      const dobDate = new Date(res.dob);
      dobDate.setDate(dobDate.getDate() + 1);
      const dobString = dobDate.toISOString().substring(0, 10);
      this.formUser.get('dob').patchValue(dobString);
      this.formUser.get('password').patchValue(null);
    });
  }

  getImageUser(id: number, avatar: string) {
    this.userImage = 'assets/image-data/user-image/' + id + '/' + avatar;
  }

  saveUser() {
    console.log(this.formUser.value);
    if (this.selectedFile) {
      this.userService
        .saveUser(this.formUser.value, this.selectedFile)
        .subscribe((res: any) => {
          if (this.formUser.get('id') !== null) {
            alert('Sửa sản phẩm thành công');
          }
          console.log(res);
        });
    } else {
      const fileToUpload = this.selectedFile || new File([], 'empty-file');
      this.userService
        .saveUser(this.formUser.value, fileToUpload)
        .subscribe((res: any) => {
          if (this.formUser.get('id') !== null) {
            alert('Sửa sản phẩm thành công');
          }
          console.log(res);
        });
    }
  }

  onFileSelectedUser(event: any) {
    const file = event.target.files[0];
    this.selectedFile = file;
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedImage = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeFileSelected() {
    this.selectedImage = 'assets/images/default-user.png';
  }

  //Order
  getAllOrderByUser(userId: number) {
    this.orderService.getAllOrderByUser(userId).subscribe((res: any) => {
      this.order = res;
    });
  }

  getAllOrderByUserAndStatus(userId: number, status: number) {
    this.orderService
      .getAllOrderByUserAndStatus(userId, status)
      .subscribe((res: any) => {
        this.order = res;
        console.log(res);
      });
  }

  getListOrderByStatus(status: number) {
    this.getAllOrderByUserAndStatus(this.userId, status);
  }

  getProductImage(productId: number, productImage: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productImage;
  }

  saveReview() {
    const review = this.formReview.value;
    console.log(this.formReview.value);
    console.log('Selected File: ', this.selectedFile);
    if (this.selectedFile) {
      this.reviewService
        .saveReview(review, this.selectedFile)
        .subscribe((res: any) => {
          if (res.id !== null) {
            alert('Đánh giá sản phẩm thành công');
            this.clickClose.nativeElement.click();
          }
        });
    } else {
      alert("Vui lòng chọn ảnh để đánh giá sản phẩm");
    }
  }

  getModalReviewProduct(orderDetail: any) {
    console.log(orderDetail);
    this.productReview = {
      productDetailId: orderDetail.productDetailId,
      name: orderDetail.name,
      image: orderDetail.image,
      color: orderDetail.color,
      size: orderDetail.size,
    };

    this.selectedFile = null;
    this.selectedImage = 'assets/images/default-user.png';
    this.formReview.get('comment').patchValue('');
    this.formReview
      .get('productDetail.id')
      .patchValue(orderDetail.productDetailId);
    this.formReview.get('order.id').patchValue(orderDetail.orderId);
    this.formReview.get('user.id').patchValue(+this.userId);
  }

  checkProductReviewed(orderId: number, productDetailId: number, userId: number): any {
    this.reviewService.checkProductReviewed(orderId, productDetailId, userId).toPromise()
    .then((res: boolean) => {
      console.log(res);
      return res;
    })
    .catch((err: any) => {
      console.error("Error Message",err);
      throw err;
    });
  }

  //Favorites
  getFavoriteByUser(pageNum: number) {
    this.route.paramMap.subscribe((params: any) => {
      this.userId = params.get('userId');
    });
    this.favoriteService
      .getPageFavoriteByUser(this.userId, pageNum)
      .subscribe((res: any) => {
        this.listFavorite = res.content;
      });
  }

  getImageProduct(productId: number, productLogo: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productLogo;
  }

  deleteProductInFavorite(productDetailId: number) {
    this.route.paramMap.subscribe((params: any) => {
      this.userId = params.get('userId');
    });
    this.favoriteService
      .deleteProductInFavoriteByUser(this.userId, productDetailId)
      .subscribe((res: any) => {
        console.log(res);

        if (res === 1) {
          this.toast.showAlertSuccess(
            'Hoàn thành',
            'Xóa thành công sản phẩm khỏi danh sách yêu thích'
          );
        } else {
          this.toast.showAlertFailure('Lỗi', 'Có lỗi xảy ra khi xóa sản phẩm');
        }
        timeout(3000);
        window.location.reload();
      });
  }

  loggout() {
    const confirm = window.confirm('Bạn có chắc chăn muốn đăng xuất không?');
    if (confirm) {
      localStorage.removeItem('accessToken');
      this.router.navigate(['/login']);
    }
  }

  selectMenuItem(menuItem: string) {
    this.selectedMenuItem = menuItem;
  }

  showModal(addressId: number) {
    this.getAddressById(addressId);
    this.isHidden = false;
  }

  hideModal() {
    this.isHidden = true;
  }

  backToTop() {
    this.scrollToTopSmoothly(0);
  }

  formatPrice(price: number) {
    return this.formatNumberWithCommas(price);
  }
}
