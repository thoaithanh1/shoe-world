import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastAlertService } from 'src/app/general-use-function/toast-alert.service';
import { Login } from 'src/app/interface/login';
import { Register } from 'src/app/interface/register';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  formLogin: any;
  formRegister: any;
  statusShowLogin: boolean = true;
  statusShowRegister: boolean = false;

  constructor(
    private accountService: AccountService,
    private toastr: ToastAlertService,
    fb: FormBuilder,
    private route: Router
  ) {
    this.formLogin = fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
    this.formRegister = fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  get username() {
    return this.formLogin.get('username');
  }
  get password() {
    return this.formLogin.get('password');
  }
  get firstname() {
    return this.formRegister.get('firstName');
  }
  get lastname() {
    return this.formRegister.get('lastName');
  }
  get email() {
    return this.formRegister.get('email');
  }
  get passwordRegister() {
    return this.formRegister.get('password');
  }

  onLogin() {
    if (this.formLogin.valid) {
      const loginData: Login = this.formLogin.value;
      this.accountService.onLogin(loginData).subscribe(
        (res: any) => {
          if (res.accessToken) {
            localStorage.setItem('accessToken', res.accessToken);
            this.route.navigate(['/home']);

            this.toastr.showAlertSuccess('Hoàn thành', 'Đăng nhập thành công!');
          }
        },
        (err) => {
          this.toastr.showAlertFailure(
            'Lỗi',
            'Đăng nhập thất bại. Vui lòng nhập lại tài khoản, mật khẩu'
          );
        }
      );
    }
  }

  onRegister() {
    if (this.formRegister.valid) {
      const registerData: Register = this.formRegister.value;
      this.accountService.onRegister(registerData).subscribe(
        (res: any) => {
          this.toastr.showAlertSuccess(
            'Hoàn thành',
            'Đăng ký tài khoản thành công'
          );
          this.toastr.showAlertInfo(
            'Xác thực',
            'Vui lòng vào email đã đăng ký để xác thực tài khoản'
          );
          this.statusShowRegister = false;
          this.statusShowLogin = true;
        },
        (err) => {
          this.toastr.showAlertFailure(
            'Lỗi',
            'Đăng ký thất bại.'
          );
        }
      );
    }
  }

  redirectLogin() {
    this.statusShowRegister = false;
    this.statusShowLogin = true;
  }

  redirectRegister() {
    this.statusShowRegister = true;
    this.statusShowLogin = false;
  }
}
