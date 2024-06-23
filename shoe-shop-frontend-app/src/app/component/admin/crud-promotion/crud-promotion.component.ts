import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AccountService } from 'src/app/services/account.service';
import { PromotionService } from 'src/app/services/promotion.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-crud-promotion',
  templateUrl: './crud-promotion.component.html',
  styleUrls: ['./crud-promotion.component.css'],
})
export class CrudPromotionComponent implements OnInit {
  statusSize: boolean = true;
  statusBrand: boolean = true;
  listPromotion: any;
  @ViewChild('keyword', { static: false }) private keyword!: ElementRef;
  statusValue: string = 'skip';
  expireValue: string = '';
  discountMethod: string = '';
  showDetailPromotion: boolean = false;
  promotionId: number = 0;
  statusModal: boolean = false;
  valueMethodPromotion: number = 1;
  valueTypePromotion: number = 1;
  formPromotion: any;
  username: string = '';

  constructor(
    private promotionService: PromotionService,
    private accountService: AccountService,
    @Inject("formatNumberWithCommas") private formatNumberWithCommas: (price: number) => string,
    private fb: FormBuilder,
  ) {
    this.formPromotion = this.fb.group({
      id: '',
      code: '',
      name: ['', Validators.required],
      discountMethod: ['1', Validators.required],
      discountType: ['1', Validators.required],
      discountValue: ['0', Validators.required],
      conditionsApply: '0',
      maxDiscountAmount: '0',
      startDate: this.formatDateForInput(new Date()),
      endDate: this.formatDateForInput(new Date()),
      createdBy: this.username,
      note: '',
      status: 'true',
    });
  }

  ngOnInit(): void {
    this.getListPromotion();
    this.getUser();
  }

  getListPromotion() {
    this.promotionService.getListPromotion().subscribe((res: any) => {
      this.listPromotion = res;
    });
  }

  getPromotion(promotionId: number) {
    this.promotionService.findPromotion(promotionId).subscribe((res: any) => {
      const promotionForm = {
        id: res.id,
        code: res.code,
        name: res.name,
        discountMethod: '' + res.discountMethod,
        discountType: '' + res.discountType,
        discountValue: '' + res.discountValue,
        conditionsApply: '' + res.conditionsApply,
        maxDiscountAmount: '' + res.maxDiscountAmount,
        startDate: this.formatTimestampToUS(res.startDate),
        endDate: this.formatTimestampToUS(res.endDate),
        createdBy: res.createdBy,
        note: res.note,
        status: '' + res.status,
      };
      if (promotionForm.discountMethod === '1') {
        this.valueMethodPromotion = 1;
      } else if (promotionForm.discountMethod === '2') {
        this.valueMethodPromotion = 2;
      }
      if (promotionForm.discountType === '1') {
        this.valueTypePromotion = 1;
      } else if (promotionForm.discountType === '2') {
        this.valueTypePromotion = 2;
      }
      this.formPromotion.patchValue(promotionForm);
    });
  }

  copyGetPromotion(promotionId: number) {
    this.promotionService.findPromotion(promotionId).subscribe((res: any) => {
      const promotionForm = {
        id: '',
        code: '',
        name: res.name,
        discountMethod: '' + res.discountMethod,
        discountType: '' + res.discountType,
        discountValue: '' + res.discountValue,
        conditionsApply: '' + res.conditionsApply,
        maxDiscountAmount: '' + res.maxDiscountAmount,
        startDate: this.formatTimestampToUS(res.startDate),
        endDate: this.formatTimestampToUS(res.endDate),
        createdBy: this.username,
        note: res.note,
        status: '' + res.status,
      };
      if (promotionForm.discountMethod === '1') {
        this.valueMethodPromotion = 1;
      } else if (promotionForm.discountMethod === '2') {
        this.valueMethodPromotion = 2;
      }
      if (promotionForm.discountType === '1') {
        this.valueTypePromotion = 1;
      } else if (promotionForm.discountType === '2') {
        this.valueTypePromotion = 2;
      }
      this.formPromotion.patchValue(promotionForm);
    });
  }

  filterPromotion() {
    const keyword = this.keyword.nativeElement.value;
    this.promotionService
      .filterPromotion(
        keyword,
        this.statusValue,
        this.discountMethod,
        this.expireValue
      )
      .subscribe((res: any) => {
        this.listPromotion = res;
      });
  }

  savePromotion() {
    if (!this.formPromotion.invalid) {
      this.promotionService
        .savePromotion(this.formPromotion.value)
        .subscribe((data) => {
          console.log(data.status);
          this.statusModal = false;
          alert('Thêm khuyến mãi thành công!');
          this.getListPromotion();
        });
    }
  }

  getUser() {
    this.accountService.getProfileUser().subscribe((res: any) => {
      this.username = res.lastName + ' ' + res.firstName;
      this.formPromotion.get('createdBy').setValue(this.username);
    });
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

  formatTimestampToUS(timestamp: number): string {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  formatDateForInput(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  formatPrice(price: number): string {
    return this.formatNumberWithCommas(price);
  }

  onDetailPromotion(id: number) {
    if (this.promotionId == id) {
      this.promotionId = 0;
    } else {
      this.promotionId = id;
    }
  }

  clickStatus(value: string) {
    this.statusValue = value;
  }

  clickExpire(value: string) {
    this.expireValue = value;
  }

  clickMethod(value: string) {
    this.discountMethod = value;
  }

  onStatusSize() {
    this.statusSize = this.statusSize ? false : true;
  }

  onStatusBrand() {
    this.statusBrand = this.statusBrand ? false : true;
  }

  onModalPromotion(promotionId: number) {
    if (promotionId !== 0) {
      this.getPromotion(promotionId);
    } else {
      const promotionFormDefault = {
        id: '',
        code: '',
        name: '',
        discountMethod: '1',
        discountType: '1',
        discountValue: '0',
        conditionsApply: '0',
        maxDiscountAmount: '0',
        startDate: this.formatDateForInput(new Date()),
        endDate: this.formatDateForInput(new Date()),
        createdBy: this.username,
        note: '',
        status: 'true',
      };
      this.formPromotion.patchValue(promotionFormDefault);
    }
    this.statusModal = this.statusModal ? false : true;
  }

  onModalPromotionByCopy(promotionId: number) {
    this.copyGetPromotion(promotionId);
    this.statusModal = this.statusModal ? false : true;
  }

  onStatusMethodPromotion(event: any) {
    this.valueMethodPromotion = event.target.value;
  }

  onStatusTypePromotion(typeNum: number) {
    this.valueTypePromotion = typeNum;
  }
}
