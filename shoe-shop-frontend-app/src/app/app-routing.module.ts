import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/user/login/login.component';
import { HomeComponent } from './component/user/home/home.component';
import { CartComponent } from './component/user/cart/cart.component';
import { CheckoutComponent } from './component/user/checkout/checkout.component';
import { ProductDetailComponent } from './component/user/product-detail/product-detail.component';
import { ProductMenuComponent } from './component/user/product-menu/product-menu.component';
import { BlogComponent } from './component/user/blog/blog.component';
import { HeaderAdminComponent } from './component/admin/header-admin/header-admin.component';
import { HomeAdminComponent } from './component/admin/home-admin/home-admin.component';
import { CrudProductComponent } from './component/admin/crud-product/crud-product.component';
import { CrudProductDetailsComponent } from './component/admin/crud-product-details/crud-product-details.component';
import { CrudUserComponent } from './component/admin/crud-user/crud-user.component';
import { CrudBrandComponent } from './component/admin/crud-brand/crud-brand.component';
import { CrudCategoryComponent } from './component/admin/crud-category/crud-category.component';
import { CrudColorComponent } from './component/admin/crud-color/crud-color.component';
import { CrudSizeComponent } from './component/admin/crud-size/crud-size.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { FavoriteComponent } from './component/user/favorite/favorite.component';
import { PageStatusPaymentComponent } from './component/user/page-status-payment/page-status-payment.component';
import { PageStatusCashPaymentComponent } from './component/user/page-status-cash-payment/page-status-cash-payment.component';
import { MyAccountComponent } from './component/user/my-account/my-account.component';
import { CrudPromotionComponent } from './component/admin/crud-promotion/crud-promotion.component';
import { CrudOrderComponent } from './component/admin/crud-order/crud-order.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    component: HomeAdminComponent,
    canActivate: [AuthenticationGuard],
    children: [
      { path: 'product-management', component: CrudProductComponent, canActivate: [AuthenticationGuard]},
      { path: 'product-detail-management', component: CrudProductDetailsComponent, canActivate: [AuthenticationGuard]},
      { path: 'promotion', component: CrudPromotionComponent, canActivate: [AuthenticationGuard]},
      { path: 'order', component: CrudOrderComponent, canActivate: [AuthenticationGuard]},
      { path: 'user-management', component: CrudUserComponent, canActivate: [AuthenticationGuard]},
      { path: 'brand-management', component: CrudBrandComponent, canActivate: [AuthenticationGuard]},
      { path: 'category-management', component: CrudCategoryComponent, canActivate: [AuthenticationGuard]},
      { path: 'color-management', component: CrudColorComponent, canActivate: [AuthenticationGuard]},
      { path: 'size-management', component: CrudSizeComponent, canActivate: [AuthenticationGuard]},
    ],
  },
  { path: 'test', component: HomeAdminComponent },
  { path: 'home', component: HomeComponent },
  { path: '', component: HomeComponent },
  {
    path: 'product-menu',
    component: ProductMenuComponent,
    data: { breadcrumb: 'Product Menu' },
  },
  {
    path: 'product-detail/:id',
    component: ProductDetailComponent,
    data: { breadcrumb: 'Product Detail' },
  },
  {
    path: 'cart/:userId',
    component: CartComponent,
    // canActivate: [AuthenticationGuard],
    data: { breadcrumb: 'Cart' },
  },
  {
    path: 'favorite/:userId',
    component: FavoriteComponent,
    canActivate: [AuthenticationGuard],
    data: { breadcrumb: 'Favorite' },
  },
  {
    path: 'checkout/:userId',
    component: CheckoutComponent,
    canActivate: [AuthenticationGuard],
    data: { breadcrumb: 'Checkout' },
  },
  {
    path: 'api/v1/payment/vnpay/response',
    component: PageStatusPaymentComponent,
    data: { breadcrumb: 'Status-VnPay-Payment' },
  },
  {
    path: 'api/v1/payment/cash/:status',
    component: PageStatusCashPaymentComponent,
    data: { breadcrumb: 'Status-Cash-Payment' },
  },
  {
    path: 'blog',
    component: BlogComponent,
    data: { breadcrumb: 'Blog' },
  },
  {
    path: 'my-account/:userId',
    component: MyAccountComponent,
    canActivate: [AuthenticationGuard],
    data: { breadcrumb: 'My Account' },
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
