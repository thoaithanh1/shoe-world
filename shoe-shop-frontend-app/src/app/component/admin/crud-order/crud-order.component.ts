import {
  Component,
  ElementRef,
  Inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { catchError, forkJoin, throwError } from 'rxjs';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-crud-order',
  templateUrl: './crud-order.component.html',
  styleUrls: ['./crud-order.component.css'],
})
export class CrudOrderComponent implements OnInit {
  listOrder: any[] = [];
  infoOrder: any = {
    id: '',
    code: '',
    createdDate: '',
    status: '',
    shippingFee: '',
    total: 0,
    note: '',
    paymentMethod: '',
    username: '',
    email: '',
    phoneNumber: '',
    address: '',
  };
  listProductOrder: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  pageSize: number = 10;
  statusOrder: number = 0;
  statusOrderSwitchTab: number = 0;
  @ViewChild('orderId', { static: false }) private orderId!: ElementRef;
  keyword: string = '';
  emptyListOrder: boolean = false;

  listOrderSelect = [{ id: '', selected: false }];
  selectAllCheckbox: boolean = false;
  statusOrderChange: number = 1;
  statusOrderSelected: number = 0;

  constructor(
    private orderService: OrderService,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (price: number) => string
  ) {}

  ngOnInit(): void {
    this.getOrderByStatus(this.currentPage, 0);
  }

  getAllOrder(pageNum: number, itemPerPage: number) {
    this.orderService
      .getAllOrder(pageNum, itemPerPage)
      .subscribe((data: any) => {
        this.listOrder = data.content;
        this.currentPage = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalPages = data.totalPages;
        this.updateDisplayedPages();
      });
  }

  getOrderByStatus(pageNum: number, status: number) {
    this.orderService
      .getOrderByStatus(pageNum, this.pageSize, status, this.keyword)
      .subscribe((data: any) => {
        this.listOrder = data.content;
        this.currentPage = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalPages = data.totalPages;
        this.statusOrderSwitchTab = status;
        this.updateDisplayedPages();
        this.listOrder = this.listOrder.map((data: any) => {
          return { ...data, selected: false };
        });
      });
  }

  getOrderByStatusSwitchTab(status: number) {
    this.keyword = '';
    this.orderService
      .getOrderByStatus(0, this.pageSize, status, this.keyword)
      .subscribe((data: any) => {
        this.listOrder = data.content;
        this.currentPage = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalPages = data.totalPages;
        this.statusOrderSwitchTab = status;
        this.statusOrder = status;
        this.updateDisplayedPages();
        this.emptyListOrder = false;
        this.listOrderSelect = [];
        this.listOrder.forEach((data: any) => {
          this.listOrderSelect.push({ id: data.id, selected: false });
        });
      });
  }

  searchOrderByCodeAndPhoneNumber(status: number) {
    this.getOrderByStatus(this.currentPage, status);
    this.emptyListOrder = this.listOrder.length < 1;
  }

  getOrderByDate(event: any) {
    this.orderService
      .getOrderByDate(
        this.currentPage,
        this.pageSize,
        event.target.value,
        this.statusOrder
      )
      .subscribe((data: any) => {
        this.listOrder = data.content;
        this.currentPage = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalPages = data.totalPages;
        this.updateDisplayedPages();
      });
    this.emptyListOrder = this.listOrder.length < 1;
  }

  getOrderDetails(orderId: number) {
    this.orderService.getOrderById(orderId).subscribe((data: any) => {
      this.infoOrder = {
        id: data.id,
        code: data.code,
        createdDate: data.createdDate,
        status: data.status,
        shippingFee: data.shippingFee,
        total: data.total,
        note: data.note,
        paymentMethod: data.paymentMethod.name,
        username: data.user.lastName + ' ' + data.user.firstName,
        email: data.user.email,
        phoneNumber: data.user.phoneNumber,
        address:
          data.address.location +
          ', ' +
          data.address.wards +
          ', ' +
          data.address.district +
          ', ' +
          data.address.city,
      };
      this.statusOrder = this.infoOrder.status;
    });
    this.orderService.getProductByOrder(orderId).subscribe((data: any) => {
      this.listProductOrder = data;
    });
  }

  getStatusOrder(event: any) {
    this.statusOrder = event.target.value;
  }

  updateStatusOrder() {
    const orderId = +this.orderId.nativeElement.value;
    this.orderService
      .updateStatusOrder(orderId, this.statusOrder)
      .subscribe((res: any) => {
        if (res.status === 'OK') {
          alert('Cập nhật đơn hàng thành công.');
          this.getAllOrder(this.currentPage, this.pageSize);
        } else {
          alert('Cập nhật đơn hàng thất bại.');
        }
      });
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
      this.getOrderByStatus(pageNumber, this.statusOrderSwitchTab);
      this.currentPage = pageNumber;
      this.updateDisplayedPages();
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

  formatPrice(price: number): string {
    return this.formatNumberWithCommas(price);
  }

  getImageProduct(productId: number, imageName: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + imageName;
  }

  toggleSelectAll() {
    this.listOrder.forEach((data) => (data.selected = this.selectAllCheckbox));
  }

  checkboxChanged() {
    if (this.isAllCheckboxesSelected()) {
      this.selectAllCheckbox = true;
    } else {
      this.selectAllCheckbox = false;
    }
    const selecteds = this.listOrder.filter((data) => data.selected);
    const firstOrder = selecteds[0].status;
    const s = selecteds.every((data) => data.status === firstOrder);
    if (s) {
      this.statusOrderSelected = firstOrder;
    } else {
      this.statusOrderSelected = 0;
    }
  }

  isAllCheckboxesSelected() {
    return this.listOrder.every((data) => data.selected);
  }

  changeStatusOrderUpdate(event: any) {
    this.statusOrderChange = event.target.value;
  }

  getSelectedOrder() {
    const selecteds = this.listOrder.filter((data) => data.selected);
    const firstOrder = selecteds[0].status;
    const s = selecteds.every((data) => data.status === firstOrder);
    if (!s) {
      alert('Vui lòng chọn các đơn hàng có cùng trạng thái');
    } else {
      // let result = false;
      // selecteds.forEach((data: any) => {
      //   this.orderService
      //     .updateStatusOrder(data.id, this.statusOrderChange)
      //     .subscribe((res: any) => {
      //       result = res.status === 'OK';
      //       if (result) this.getAllOrder(this.currentPage, this.pageSize);
      //     });
      // });
      const observables = selecteds.map((data: any) => {
        return this.orderService.updateStatusOrder(data.id, this.statusOrderChange).pipe(
          catchError((error) => {
            return throwError(error);
          })
        );
      });
      forkJoin(observables).subscribe((results: any[]) => {
        const allSuccessful = results.every((res) => res.status === 'OK');
        if (allSuccessful) {
          alert('Cập nhật đơn hàng thành công.');
          this.getAllOrder(this.currentPage, this.pageSize);
        } else {
          alert('Cập nhật đơn hàng thất bại.');
        }
      });
      // if (result) {
      //   alert('Cập nhật đơn hàng thành công.');
      //   this.getAllOrder(this.currentPage, this.pageSize);
      // } else {
      //   alert('Cập nhật đơn hàng thất bại.');
      // }
    }
  }
}
