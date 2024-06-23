import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxStarRatingModule } from 'ngx-star-rating';

import { AppComponent } from './app.component';
import { HomeComponent } from './component/user/home/home.component';
import { LoginComponent } from './component/user/login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { CartComponent } from './component/user/cart/cart.component';
import { CheckoutComponent } from './component/user/checkout/checkout.component';
import { ProductDetailComponent } from './component/user/product-detail/product-detail.component';
import { ProductMenuComponent } from './component/user/product-menu/product-menu.component';
import { HeaderComponent } from './component/user/header/header.component';
import { BreadcrumbModule } from 'angular-crumbs';
import { BlogComponent } from './component/user/blog/blog.component';
import { FooterComponent } from './component/user/footer/footer.component';

import {register} from 'swiper/element/bundle';
import { HeaderAdminComponent } from './component/admin/header-admin/header-admin.component';
import { HomeAdminComponent } from './component/admin/home-admin/home-admin.component';
import { CrudProductComponent } from './component/admin/crud-product/crud-product.component';
import { CrudProductDetailsComponent } from './component/admin/crud-product-details/crud-product-details.component';
import { CrudUserComponent } from './component/admin/crud-user/crud-user.component';
import { CrudBrandComponent } from './component/admin/crud-brand/crud-brand.component';
import { CrudCategoryComponent } from './component/admin/crud-category/crud-category.component';
import { CrudColorComponent } from './component/admin/crud-color/crud-color.component';
import { CrudSizeComponent } from './component/admin/crud-size/crud-size.component';
import { GeneralUseFunctionModule } from './general-use-function/general-use-function.module';
import { AuthenticationInterceptor } from './interceptor/authentication.interceptor';
import { NgxImageZoomModule } from 'ngx-image-zoom';
import { FavoriteComponent } from './component/user/favorite/favorite.component';
import { PageStatusPaymentComponent } from './component/user/page-status-payment/page-status-payment.component';
import { PageStatusCashPaymentComponent } from './component/user/page-status-cash-payment/page-status-cash-payment.component';
import { MyAccountComponent } from './component/user/my-account/my-account.component';
import { CrudPromotionComponent } from './component/admin/crud-promotion/crud-promotion.component';
import { CrudOrderComponent } from './component/admin/crud-order/crud-order.component';

register();

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    CartComponent,
    CheckoutComponent,
    ProductDetailComponent,
    ProductMenuComponent,
    HeaderComponent,
    BlogComponent,
    FooterComponent,
    HeaderAdminComponent,
    HomeAdminComponent,
    CrudProductComponent,
    CrudProductDetailsComponent,
    CrudUserComponent,
    CrudBrandComponent,
    CrudCategoryComponent,
    CrudColorComponent,
    CrudSizeComponent,
    FavoriteComponent,
    PageStatusPaymentComponent,
    PageStatusCashPaymentComponent,
    MyAccountComponent,
    CrudPromotionComponent,
    CrudOrderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BreadcrumbModule,
    HttpClientModule,
    ReactiveFormsModule,
    GeneralUseFunctionModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgxImageZoomModule,
    NgxStarRatingModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
