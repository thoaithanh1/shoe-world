import {
  Component,
  ElementRef,
  HostListener,
  Inject,
  OnInit,
  Renderer2,
} from '@angular/core';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { CartDB } from 'src/app/interface/cart';
import { Favorite } from 'src/app/interface/favorite';
import { AccountService } from 'src/app/services/account.service';
import { BrandService } from 'src/app/services/brand.service';
import { CartService } from 'src/app/services/cart.service';
import { CategoryService } from 'src/app/services/category.service';
import { FavoriteService } from 'src/app/services/favorite.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  categoriesSliders = [
    '/assets/images/categories/cat-1.jpg',
    '/assets/images/categories/cat-2.jpg',
    '/assets/images/categories/cat-3.jpg',
    '/assets/images/categories/cat-4.jpg',
    '/assets/images/categories/cat-5.jpg',
    '/assets/images/categories/cat-1.jpg',
    '/assets/images/categories/cat-2.jpg',
    '/assets/images/categories/cat-3.jpg',
    '/assets/images/categories/cat-4.jpg',
    '/assets/images/categories/cat-5.jpg',
    '/assets/images/categories/cat-1.jpg',
    '/assets/images/categories/cat-2.jpg',
    '/assets/images/categories/cat-3.jpg',
    '/assets/images/categories/cat-4.jpg',
    '/assets/images/categories/cat-5.jpg',
  ];

  bannerImage = '/assets/images/banner.jpg';

  listBrand: any;
  listCategories: any;
  pageProducts: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  brandId: number = 0;
  userId: number = 0;
  formFavorite: any;
  scrollY: number = 0;

  constructor(
    private renderer: Renderer2,
    private el: ElementRef,
    private productService: ProductService,
    private brandService: BrandService,
    private categoryService: CategoryService,
    private accountService: AccountService,
    private favoriteService: FavoriteService,
    private cartService: CartService,
    private toast: ToastAlertService,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (inputNumber: number) => string,
    @Inject('scrollToTopSmoothly')
    private scrollToTopSmoothly: (inputNumber: number) => void
  ) {}

  ngOnInit(): void {
    this.getListBrand();
    this.getListCategory();
    this.getPageProductDisplayHome(this.currentPage, this.brandId);
    if (localStorage.getItem('accessToken')) {
      this.getUser();
    }
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    this.scrollY = window.scrollY;
  }

  getUser() {
    this.accountService.getProfileUser().subscribe((res: any) => {
      this.userId = res.userId;
      if (sessionStorage.getItem('cart')) {
        const cartSession = JSON.parse(sessionStorage.getItem('cart')!);
        if (cartSession.length !== 0) {
          cartSession.forEach((item: any) => {
            const formProductCart: CartDB = {
              userId: this.userId,
              productDetailId: item.productDetailId,
              quantity: item.quantity,
              totalPrice: item.totalPrice * item.quantity,
            };
            this.cartService
              .addProductToCartDB(formProductCart)
              .subscribe((data) => {
                if (data.status === 'Successfully') {
                  console.log(data);
                  sessionStorage.removeItem('cart');
                }
              });
          });
        }
      }
    });
  }

  getPageProductDisplayHome(pageNum: number, brandId: number) {
    this.productService
      .getPageProductDisplayHome(pageNum, brandId)
      .subscribe((res: any) => {
        this.pageProducts = res.content;
        this.currentPage = res.pageable.pageNumber;
        this.totalPages = res.totalPages;
        this.updateDisplayedPages();
      });
  }

  getPageProductDisplayHomeByBranId(brandId: number) {
    this.productService
      .getPageProductDisplayHome(this.currentPage, brandId)
      .subscribe((res: any) => {
        this.pageProducts = res.content;
        this.currentPage = res.pageable.pageNumber;
        this.totalPages = res.totalPages;
        this.updateDisplayedPages();
      });
  }

  getListBrand() {
    this.brandService.getListBrand().subscribe((res: any) => {
      this.listBrand = res;
    });
  }

  getListCategory() {
    this.categoryService.getListCategory().subscribe((res: any) => {
      this.listCategories = res;
    });
  }

  saveFavoriteByUser(productDetailId: number) {
    const favorite: Favorite = (this.formFavorite = {
      userId: this.userId,
      productDetailId: productDetailId,
    });
    console.log(favorite);

    this.favoriteService.saveFavoriteByUser(favorite).subscribe((res: any) => {
      if (res) {
        this.toast.showAlertSuccess(
          'Hoàn thành',
          'Đã thêm sản phẩm vào danh sách yêu thích'
        );
      } else {
        this.toast.showAlertFailure(
          'Lỗi',
          'Có lỗi xảy ra khi thêm sản phẩm vào danh sách yêu thích'
        );
      }
    });
  }

  getImageProduct(productId: number, productLogo: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productLogo;
  }

  getImageBrand(brandId: number, brandLogo: string): string {
    return 'assets/image-data/brand-image/' + brandId + '/' + brandLogo;
  }

  updateDisplayedPages() {
    this.totalPagesArray = [];
    const startPage = Math.max(this.currentPage - 1, 1);
    const endPage = Math.min(startPage + 2, this.totalPages);
    for (let i = startPage; i <= endPage; i++) {
      this.totalPagesArray.push(i);
    }
  }

  switchPage(pageNumber: number) {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.getPageProductDisplayHome(pageNumber, this.brandId);
      this.currentPage = pageNumber;
      this.updateDisplayedPages();
      this.scrollToTopSmoothly(1000);
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.switchPage(this.currentPage - 1);
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.switchPage(this.currentPage + 1);
    }
  }

  toggleCategories() {
    const ulElement = this.el.nativeElement.querySelector(
      '.hero-categories ul'
    );
    if (ulElement) {
      const isHidden =
        ulElement.style.display === 'none' || ulElement.style.display === '';
      this.renderer.setStyle(ulElement, 'display', isHidden ? 'block' : 'none');
    }
  }

  formatNumber(number: number): string {
    return this.formatNumberWithCommas(number);
  }

  backToTop() {
    this.scrollToTopSmoothly(0);
  }
}
