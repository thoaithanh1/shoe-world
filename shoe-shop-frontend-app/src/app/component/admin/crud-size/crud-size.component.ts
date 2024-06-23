import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Size } from 'src/app/interface/size';
import { SizeService } from 'src/app/services/size.service';

@Component({
  selector: 'app-crud-size',
  templateUrl: './crud-size.component.html',
  styleUrls: ['./crud-size.component.css'],
})
export class CrudSizeComponent {
  listSize: any;
  formSize: any;

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm Kích Cỡ';
  buttonName: string = 'Thêm';

  constructor(private sizeService: SizeService, private fb: FormBuilder) {
    this.formSize = this.fb.group({
      id: '',
      name: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.getPageSize(this.currentPage);
  }

  get name() {
    return this.formSize.get('name');
  }

  getPageSize(pageNum: number) {
    this.sizeService.getPageSize(pageNum).subscribe((res: any) => {
      this.listSize = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }
  
  getSize(sizeId: number) {
    this.titleName = 'Sửa Kích Cỡ';
    this.buttonName = 'Sửa';
    this.sizeService.getSize(sizeId).subscribe((res: any) => {
      this.formSize.patchValue(res);
    });
  }

  saveSize() {
    const size: Size = this.formSize.value;
    this.sizeService.saveSize(size).subscribe(() => {
      if (this.formSize.get('id') === null) {
        alert('Save size successfully');
      } else {
        alert('Update size successfully');
      }
      this.getPageSize(this.currentPage);
      this.formSize.reset();
    });
  }

  resetForm() {
    this.titleName = 'Thêm Kích Cỡ';
    this.buttonName = 'Thêm';
    this.formSize.reset();
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
      this.getPageSize(pageNumber);
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
