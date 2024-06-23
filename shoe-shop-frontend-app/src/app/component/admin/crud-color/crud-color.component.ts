import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Color } from 'src/app/interface/color';
import { ColorService } from 'src/app/services/color.service';

@Component({
  selector: 'app-crud-color',
  templateUrl: './crud-color.component.html',
  styleUrls: ['./crud-color.component.css'],
})
export class CrudColorComponent {
  listColor: any;
  formColor: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm người dùng';
  buttonName: string = 'Thêm';

  constructor(private colorService: ColorService, private fb: FormBuilder) {
    this.formColor = this.fb.group({
      id: '',
      name: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.getPageColor(this.currentPage);
  }

  get name() {
    return this.formColor.get('name');
  }

  getPageColor(pageNum: number) {
    this.colorService.getPageColor(pageNum).subscribe((res: any) => {
      this.listColor = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }

  getColor(colorId: number) {
    this.titleName = 'Sửa Màu Sắc';
    this.buttonName = 'Sửa';
    this.colorService.getColor(colorId).subscribe((res: any) => {
      this.formColor.patchValue(res);
    });
  }

  saveColor() {
    const color: Color = this.formColor.value;
    this.colorService.saveColor(color).subscribe(() => {
      if (this.formColor.get('id') === null) {
        alert('Save color successfully');
      } else {
        alert('Update color successfully');
      }
      this.getPageColor(this.currentPage);
      this.formColor.reset();
    });
  }

  resetForm() {
    this.titleName = 'Thêm Màu Sắc';
    this.buttonName = 'Thêm';
    this.formColor.reset();
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
      this.getPageColor(pageNumber);
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
