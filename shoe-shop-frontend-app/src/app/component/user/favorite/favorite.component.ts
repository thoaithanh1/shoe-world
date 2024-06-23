import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { timeout } from 'rxjs';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { FavoriteService } from 'src/app/services/favorite.service';

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.css'],
})
export class FavoriteComponent implements OnInit {
  listFavorite: any;
  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  userId: number = 0;
  constructor(
    private route: ActivatedRoute,
    private favoriteService: FavoriteService,
    private toast: ToastAlertService,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (price: number) => string
  ) {}

  ngOnInit(): void {
    this.getFavoriteByUser(this.currentPage);
  }

  getFavoriteByUser(pageNum: number) {
    this.route.paramMap.subscribe((params: any) => {
      this.userId = params.get('userId');
    });
    this.favoriteService
      .getPageFavoriteByUser(this.userId, pageNum)
      .subscribe((res: any) => {
        this.listFavorite = res.content;
        this.currentPage = res.pageable.pageNumber;
        this.totalPages = res.totalPages;
        this.updateDisplayedPages();
      });
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

  getImageProduct(productId: number, productLogo: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + productLogo;
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
      this.getFavoriteByUser(pageNumber);
      this.currentPage = pageNumber;
      this.updateDisplayedPages();
      // this.scrollToTopSmoothly(1000);
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

  formatPrice(price: number) {
    return this.formatNumberWithCommas(price);
  }
}
