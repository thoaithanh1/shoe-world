import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Product } from 'src/app/interface/product';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-crud-product',
  templateUrl: './crud-product.component.html',
  styleUrls: ['./crud-product.component.css'],
})
export class CrudProductComponent implements OnInit {
  listProduct: any;
  listBrand: any;
  listCategory: any;
  formProduct: any;
  formProductDetails: any;
  productImageEdit: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];

  constructor(
    private productService: ProductService,
    private brandService: BrandService,
    private categoryService: CategoryService,
    fb: FormBuilder
  ) {
    this.formProduct = fb.group({
      id: '',
      name: ['', Validators.required],
      material: ['', Validators.required],
      model: ['', Validators.required],
      description: ['', Validators.required],
      brand: fb.group({
        id: ['', Validators.required],
      }),
      category: fb.group({
        id: ['', Validators.required],
      }),
    });

    this.formProductDetails = fb.group({
      id: '',
      price: ['', Validators.required],
      gender: ['', Validators.required],
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
    this.getListProduct(this.currentPage);
    this.getListBrand();
    this.getListCategory();
  }

  get name() {
    return this.formProduct.get('name');
  }
  get material() {
    return this.formProduct.get('material');
  }
  get model() {
    return this.formProduct.get('model');
  }
  get description() {
    return this.formProduct.get('description');
  }
  get brand() {
    return this.formProduct.get('brand.id');
  }
  get category() {
    return this.formProduct.get('category.id');
  }

  getListProduct(pageNum: number) {
    this.productService.getListProductByPage(pageNum).subscribe((res: any) => {
      this.listProduct = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }

  getListBrand() {
    this.listBrand = this.brandService.getListBrand().subscribe((res: any) => {
      this.listBrand = res;
    });
  }

  getListCategory() {
    this.listCategory = this.categoryService
      .getListCategory()
      .subscribe((res: any) => {
        this.listCategory = res;
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
      this.getListProduct(pageNumber);
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

  getProduct(productId: number) {
    this.productService.getProduct(productId).subscribe((res: any) => {
      this.formProduct.patchValue(res);
    });
  }

  addProduct() {
    if (this.formProduct.valid) {
      const productData: Product = this.formProduct.value;
      this.productService.saveProduct(productData).subscribe((res: any) => {
        console.log(res);
        if (this.formProduct.get('id') === null) {
          alert('Thêm sản phẩm thành công');
        } else {
          alert('Sửa sản phẩm thành công');
        }
        this.getListProduct(this.currentPage);
        this.resetForm();
      });
    }
  }

  resetForm() {
    this.formProduct.reset();
  }
}
