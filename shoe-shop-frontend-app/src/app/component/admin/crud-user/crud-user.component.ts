import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
import { User } from 'src/app/interface/user';
import { RoleService } from 'src/app/services/role.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-crud-user',
  templateUrl: './crud-user.component.html',
  styleUrls: ['./crud-user.component.css'],
})
export class CrudUserComponent implements OnInit {
  listUser: any;
  listRole: any;
  formUser: any;
  formRole: any;

  userImage = 'assets/images/default-user.png';

  currentPage: number = 0;
  totalPages: number = 0;
  totalPagesArray: number[] = [];
  titleName: string = 'Thêm người dùng';
  buttonName: string = 'Thêm';

  selectedImage: string | ArrayBuffer | null = 'assets/images/default-user.png';
  selectedFile: File | null = null;

  constructor(
    private userService: UserService,
    private roleService: RoleService,
    private fb: FormBuilder
  ) {
    this.formUser = fb.group({
      id: '',
      avatar: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      dob: ['', Validators.required],
      gender: ['', Validators.required],
      status: ['', Validators.required],
      roles: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.getPageUser(this.currentPage);
    this.getListRole();
    console.log(this.formRole);
  }

  get avatar() {
    return this.formUser.get('avatar');
  }
  get firstName() {
    return this.formUser.get('firstName');
  }
  get lastName() {
    return this.formUser.get('lastName');
  }
  get email() {
    return this.formUser.get('email');
  }
  get password() {
    return this.formUser.get('password');
  }
  get phoneNumber() {
    return this.formUser.get('phoneNumber');
  }
  get dob() {
    return this.formUser.get('dob');
  }
  get gender() {
    return this.formUser.get('gender');
  }
  get status() {
    return this.formUser.get('status');
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
      this.getPageUser(pageNumber);
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

  getPageUser(pageNum: number) {
    this.userService.getListUser(pageNum).subscribe((res: any) => {
      console.log(res);
      this.listUser = res.content;
      this.currentPage = res.pageable.pageNumber;
      this.totalPages = res.totalPages;
      this.updateDisplayedPages();
    });
  }

  getListRole() {
    this.roleService.getListRoles().subscribe((res: any) => {
      this.listRole = res;
    });
  }

  getUserImage(userId: number, imageName: string): string {
    if(imageName == null) {
      return this.userImage = 'assets/images/default-user.png';
    }
    return this.userImage = 'assets/image-data/user-image/' + userId + '/' + imageName;
  }

  getUser(userId: number) {
    this.titleName = 'Sửa người dùng';
    this.buttonName = 'Sửa';
    this.userService.getUser(userId).subscribe((res: any) => {
      this.formUser.patchValue(res);
      console.log(this.formUser);
    });
  }

  addUser() {
    const userDatas: User = this.formUser.value;
    if (this.selectedFile !== null) {
      this.userService
        .saveUser(userDatas, this.selectedFile)
        .subscribe(() => {
          if (this.formUser.get('id') === null) {
            alert('Thêm sản phẩm thành công');
          } else {
            alert('Sửa sản phẩm thành công');
          }
          this.getPageUser(this.currentPage);
          this.resetForm();
        });
    }
  }

  resetForm() {
    this.formUser.reset();
    this.titleName = 'Thêm người dùng';
    this.buttonName = 'Thêm';
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.selectedFile = file;
    if (file) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.selectedImage = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }

  onCheckChangeRole(event: any) {
    const formArray: FormArray = this.formUser.get('roles') as FormArray;

    if (event.target.checked) {
      formArray.push(this.fb.group({ id: +event.target.value }));
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
  }
}
