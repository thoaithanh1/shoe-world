import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ProductDetail } from 'src/app/interface/product-detail';
import { ColorService } from 'src/app/services/color.service';
import { ProductDetailService } from 'src/app/services/product-detail.service';
import { ProductService } from 'src/app/services/product.service';
import { SizeService } from 'src/app/services/size.service';

@Component({
  selector: 'app-crud-product-details',
  templateUrl: './crud-product-details.component.html',
  styleUrls: ['./crud-product-details.component.css'],
})
export class CrudProductDetailsComponent implements OnInit {
  @ViewChild('mainImage', { static: false }) mainImageInput!: ElementRef;
  @ViewChild('subImage', { static: false }) subImageInput!: ElementRef;
  listProductDetail: any;
  listProduct: any;
  listColor: any;
  listSize: any;
  formProductDetail: any;

  productFile: File | null = null;
  selectedImage: string = 'assets/images/default-user.png';

  subFiles: File[] = [];
  subImages: string[] = [];

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm sản phẩm';
  buttonName: string = 'Thêm';

  constructor(
    private productDetailService: ProductDetailService,
    private productService: ProductService,
    private colorService: ColorService,
    private sizeService: SizeService,
    fb: FormBuilder
  ) {
    this.formProductDetail = fb.group({
      id: '',
      quantity: [
        '',
        [Validators.required, Validators.min(1), Validators.max(1000000)],
      ],
      price: [
        '',
        [Validators.required, Validators.min(1000), Validators.max(1000000000)],
      ],
      gender: [+'', Validators.required],
      status: ['', Validators.required],
      product: fb.group({
        id: ['', Validators.required],
      }),
      color: fb.group({
        id: ['', Validators.required],
      }),
      size: fb.group({
        id: ['', Validators.required],
      }),
    });
  }

  ngOnInit(): void {
    this.getListProductDetail(this.currentPage);
    this.getListProduct();
    this.getListColor();
    this.getListSize();
  }

  get quantity() {
    return this.formProductDetail.get('quantity');
  }
  get price() {
    return this.formProductDetail.get('price');
  }
  get gender() {
    return this.formProductDetail.get('gender');
  }
  get status() {
    return this.formProductDetail.get('status');
  }
  get product() {
    return this.formProductDetail.get('product.id');
  }
  get color() {
    return this.formProductDetail.get('color.id');
  }
  get size() {
    return this.formProductDetail.get('size.id');
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
      this.getListProductDetail(pageNumber);
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

  getListProductDetail(pageNumber: number) {
    this.productDetailService
      .getListProductDetailByPage(pageNumber)
      .subscribe((res: any) => {
        this.listProductDetail = res.content;
        this.currentPage = res.pageable.pageNumber;
        this.totalPages = res.totalPages;
        this.updateDisplayedPages();
      });
  }

  getListProduct() {
    this.productService.getListProduct().subscribe((res: any) => {
      this.listProduct = res;
    });
  }

  getListColor() {
    this.colorService.getListColor().subscribe((res: any) => {
      this.listColor = res;
    });
  }

  getListSize() {
    this.sizeService.getListSize().subscribe((res: any) => {
      this.listSize = res;
    });
  }

  getProductDetail(productDetailId: number) {
    this.titleName = 'Sửa sản phẩm';
    this.buttonName = 'Sửa';
    this.productDetailService
      .getProductDetail(productDetailId)
      .subscribe((res: any) => {
        this.formProductDetail.patchValue(res);
      });
  }

  getImageProduct(productId: number, imageName: string): string {
    return 'assets/image-data/product-image/' + productId + '/' + imageName;
  }

  addProductDetail() {
    if (this.productFile && this.subFiles) {
      const productData: ProductDetail = this.formProductDetail.value;
      const formData: FormData = new FormData();
      formData.append(
        'productDetail',
        new Blob([JSON.stringify(productData)], { type: 'application/json' })
      );
      formData.append('productImage', this.productFile);
      for (let i = 0; i < this.subFiles.length; i++) {
        formData.append('productSubImage', this.subFiles[i]);
      }
      this.productDetailService
        .saveProductDetail(formData)
        .subscribe((res: any) => {
          if (this.formProductDetail.get('id') === null) {
            alert('Thêm sản phẩm thành công');
          } else {
            alert('Sửa sản phẩm thành công');
          }
          this.getListProductDetail(this.currentPage);
          this.resetForm();
          this.subFiles = [];
          this.subImages = [];
          this.selectedImage = 'assets/images/default-user.png';
        });
    }
  }

  resetForm() {
    this.formProductDetail.reset();
    this.mainImageInput.nativeElement.value = '';
    this.subImageInput.nativeElement.value = '';
    this.titleName = 'Thêm sản phẩm';
    this.buttonName = 'Thêm';
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.productFile = file;
    if (file) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.selectedImage = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }

  onSubFileSelected(event: any) {
    const files = event.target.files;

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      this.subFiles.push(files[i]);
      
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          this.subImages.push(e.target.result);
          
        };

        reader.readAsDataURL(file);
      }
    }
  }
}
