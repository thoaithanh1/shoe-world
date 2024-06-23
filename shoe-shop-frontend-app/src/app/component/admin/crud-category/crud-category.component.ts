import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Category } from 'src/app/interface/category';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-crud-category',
  templateUrl: './crud-category.component.html',
  styleUrls: ['./crud-category.component.css'],
})
export class CrudCategoryComponent {
  listCategory: any;
  formCategory: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm Thể Loại';
  buttonName: string = 'Thêm';

  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder
  ) {
    this.formCategory = this.fb.group({
      id: '',
      name: ['', Validators.required],
    });
  }

  get name() {
    return this.formCategory.get('name');
  }

  ngOnInit(): void {
    this.getPageCategory(this.currentPage);
  }

  getPageCategory(pageNum: number) {
    this.categoryService.getPageCategory(pageNum).subscribe((res: any) => {
      this.listCategory = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }

  getCategory(categoryId: number) {
    this.categoryService.getCategory(categoryId).subscribe((res: any) => {
      this.formCategory.patchValue(res);
    });
  }

  saveCategory() {
    this.titleName = 'Sửa Thể Loại';
    this.buttonName = 'Sửa';
    const category: Category = this.formCategory.value;
    this.categoryService.saveCategory(category).subscribe(() => {
      if (this.formCategory.get('id') === null) {
        alert('Save category successfully');
      } else {
        alert('Update category successfully');
      }
      this.formCategory.reset();
      this.getPageCategory(this.currentPage);
    });
  }

  resetForm() {
    this.titleName = 'Thêm Thể Loại';
    this.buttonName = 'Thêm';
    this.formCategory.reset();
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
      this.getPageCategory(pageNumber);
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
}
