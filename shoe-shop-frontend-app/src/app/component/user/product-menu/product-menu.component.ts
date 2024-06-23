import { Component, ElementRef, Inject, OnInit, Renderer2, ViewChild } from '@angular/core';
import { FormArray, FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { BrandService } from 'src/app/services/brand.service';
import { ProductService } from 'src/app/services/product.service';
import { SizeService } from 'src/app/services/size.service';

@Component({
  selector: 'app-product-menu',
  templateUrl: './product-menu.component.html',
  styleUrls: ['./product-menu.component.css'],
})
export class ProductMenuComponent implements OnInit{
  productSliders: string[] = [
    '/assets/images/product-detail/product/product-1.jpg',
    '/assets/images/product-detail/product/product-2.jpg',
    '/assets/images/product-detail/product/product-3.jpg',
    '/assets/images/product-detail/product/product-1.jpg',
    '/assets/images/product-detail/product/product-2.jpg',
    '/assets/images/product-detail/product/product-3.jpg',
    '/assets/images/product-detail/product/product-1.jpg',
    '/assets/images/product-detail/product/product-2.jpg',
    '/assets/images/product-detail/product/product-3.jpg',
  ];

  formProductFilter: any;
  pageProducts: any;
  listSize: any;
  listBrand: any;

  totalElements: number = 0;
  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];

  moneyMin: number = 0;
  moneyMax: number = 15000000;
  @ViewChild("keyword", {static: false}) private keyword!: ElementRef;

  statusSize: boolean = false;
  statusBrand: boolean = false;
  statusGender: boolean = false;

  constructor(
    private productService: ProductService,
    private sizeService: SizeService,
    private brandService: BrandService,
    private renderer: Renderer2, 
    private el: ElementRef,
    private fb: FormBuilder,
    @Inject("formatNumberWithCommas") private formatNumberWithCommas: (inputNumber: number) => string,
    @Inject("scrollToTopSmoothly") private scrollToTopSmoothly: (endNumber: number) => void,
    ){
      this.formProductFilter = this.fb.group({
        pageNum: +'',
        orderBy: 'min-to-max',
        keyword: '',
        gender: +'null',
        brandList: this.fb.array([]),
        sizeList: this.fb.array([]),
        minPrice: +'',
        maxPrice: +'',
      });
    }

  ngOnInit(): void {
    // this.route.paramMap.subscribe((params) => {
    //   const brandId = params.get('brandId');
    //   const formArray: FormArray = this.formProductFilter.get('brandList') as FormArray;
    //   if(brandId) {
    //     formArray.push(this.fb.control(+brandId ));
    //   }
    //   this.filterProduct();
    // });
    this.getPageProductDisplayHome(this.currentPage);
    this.getListSize();
    this.getListBrand();
  }

  filterProduct(){
    this.formProductFilter.get('keyword').setValue(this.keyword.nativeElement.value);
    const formProduct = this.formProductFilter.value;
    this.productService.filterProduct(formProduct).subscribe((res: any) => {
      console.log(res);
      this.pageProducts = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.totalElements = res.totalElements;
      this.updateDisplayedPages();
      // this.scrollToTopSmoothly(650);
    });
  }

  getPageProductDisplayHome(pageNum: number) {
    this.productService.getPageProductDisplayHome(pageNum, 0).subscribe((res: any) => {
      this.pageProducts = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.totalElements = res.totalElements;
      this.updateDisplayedPages();
      this.keyword.nativeElement.value = "";
      // this.scrollToTopSmoothly(0);
    });
  }

  getImageProduct(productId: number, productLogo: string): string {
    return "assets/image-data/product-image/" + productId + "/" + productLogo;
  }

  getListSize(){
    this.sizeService.getListSize().subscribe((res: any) => {
      this.listSize = res;
    });
  }

  getListBrand(){
    this.brandService.getListBrand().subscribe((res: any) => {
      this.listBrand = res;
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
      this.getPageProductDisplayHome(pageNumber);
      this.currentPage = pageNumber;
      this.updateDisplayedPages();
      // this.scrollToTopSmoothly(650);
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

  formatNumber(number: number): string {
    return this.formatNumberWithCommas(number);
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

  onCheckChangeSize(event: any) {
    const formArray: FormArray = this.formProductFilter.get('sizeList') as FormArray;

    if (event.target.checked) {
      formArray.push(this.fb.control(+event.target.value ));
    } else {
      let i: number = 0;

      formArray.controls.forEach((ctrl: any) => {
        if (ctrl.value == event.target.value) {
          formArray.removeAt(i);
          return;
        }
        i++;
      });
    }
    this.filterProduct();
  }
  onCheckChangeBrand(event: any) {
    const formArray: FormArray = this.formProductFilter.get('brandList') as FormArray;

    if (event.target.checked) {
      formArray.push(this.fb.control(+event.target.value ));
    } else {
      let i: number = 0;

      formArray.controls.forEach((ctrl: any) => {
        if (ctrl.value == event.target.value) {
          formArray.removeAt(i);
          return;
        }
        i++;
      });
    }
    this.filterProduct();
  }

  onStatusSize() {
    this.statusSize = this.statusSize ? false : true;
    this.statusBrand = this.statusBrand ? false : false;
    this.statusGender = this.statusGender ? false : false;
  }

  onStatusBrand() {
    this.statusBrand = this.statusBrand ? false : true;
    this.statusSize = this.statusSize ? false : false;
    this.statusGender = this.statusGender ? false : false;
  }
  onStatusGender() {
    this.statusGender = this.statusGender ? false : true;
    this.statusBrand = this.statusBrand ? false : false;
    this.statusSize = this.statusSize ? false : false;
  }
}
