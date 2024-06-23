import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Brand } from 'src/app/interface/brand';
import { BrandService } from 'src/app/services/brand.service';

@Component({
  selector: 'app-crud-brand',
  templateUrl: './crud-brand.component.html',
  styleUrls: ['./crud-brand.component.css'],
})
export class CrudBrandComponent implements OnInit {
  listBrand: any;
  formBrand: any;
  brandFile: File | null = null;
  selectedImage: string | ArrayBuffer | null = 'assets/images/default-user.png';

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm thương hiệu';
  buttonName: string = 'Thêm';

  constructor(private brandService: BrandService, private fb: FormBuilder) {
    this.formBrand = this.fb.group({
      id: '',
      name: ['', Validators.required],
    });
  }

  get name() {
    return this.formBrand.get('name');
  }

  ngOnInit(): void {
    this.getPageBrand(this.currentPage);
  }

  getPageBrand(pageNum: number) {
    this.brandService.getPageBrand(pageNum).subscribe((res: any) => {
      this.listBrand = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }

  getBrand(brandId: number) {
    this.titleName = 'Sửa thương hiệu';
    this.buttonName = 'Sửa';
    this.brandService.getBrand(brandId).subscribe((res: any) =>{
      this.formBrand.patchValue(res);
    });
  }

  getImageBrand(brandId: number, logoBrand: string): string {
    return "assets/image-data/brand-image/" + brandId + "/" + logoBrand;
  }

  saveBrand() {
    const brand: Brand = this.formBrand.value;
    if (this.brandFile) {
      this.brandService.saveBrand(brand, this.brandFile).subscribe(() => {
        if (this.formBrand.get('id') === null) {
          alert('Save brand successfully');
        } else {
          alert('Update brand successfully');
        }
        this.formBrand.reset();
        this.getPageBrand(this.currentPage);
      });
    }
  }

  resetForm() {
    this.formBrand.reset();
    this.titleName = 'Thêm thương hiệu';
    this.buttonName = 'Thêm';
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
      this.getPageBrand(pageNumber);
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

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.brandFile = file;
    if (file) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.selectedImage = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }
}
