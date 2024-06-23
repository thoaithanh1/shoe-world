import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartSession } from 'src/app/interface/cart-session';
import { AccountService } from 'src/app/services/account.service';
import { CartService } from 'src/app/services/cart.service';
import { FavoriteService } from 'src/app/services/favorite.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isAuthenticated: boolean = false;
  userId: number = 0;
  userAvatar: string = 'assets/images/default.png';
  countProductInCart: number = 0;
  countProductInFavorite: number = 0;
  totalPrice: number = 0;

  constructor(
    private userService: UserService,
    private accountService: AccountService,
    private cartService: CartService,
    private favoriteService: FavoriteService,
    private router: Router,
    @Inject('formatNumberWithCommas')
    private formatNumberWithCommas: (number: number) => string
  ) {}

  ngOnInit(): void {
    if (localStorage.getItem('accessToken')) {
      this.checkAuthenticated();
      this.getUser();
    }
  }

  checkAuthenticated() {
    this.userService.isAuthenticated().subscribe((res) => {
      this.isAuthenticated = res;
    });
  }

  getUser() {
    this.accountService.getProfileUser().subscribe((res: any) => {
      this.userId = res.userId;
      this.userAvatar = res.avatar;
      this.getImageUser(this.userId, this.userAvatar);
      this.countProductInCarts(this.userId);
      this.countProductInFavorites(this.userId);
      this.totalPriceInCart(this.userId);
    });
  }

  countProductInCarts(userId: number) {
    this.cartService.countProductInCartByUser(userId).subscribe((res) => {
      this.countProductInCart = res;
    });
  }

  countProductInFavorites(userId: number) {
    this.favoriteService.getCountProductInFavorite(userId).subscribe((res) => {
      this.countProductInFavorite = res;
    });
  }

  totalPriceInCart(userId: number) {
    this.cartService.totalPriceInCartByUser(userId).subscribe((res) => {
      this.totalPrice = res;
    });
  }

  getImageUser(id: number, avatar: string): string {
    return 'assets/image-data/user-image/' + id + '/' + avatar;
  }

  loggout() {
    localStorage.removeItem("accessToken");
    this.router.navigate(['/login']);
  }

  // showProductInCart() {
  //   const cartJSON = sessionStorage.getItem('cart');
  //   if (cartJSON) {
  //     const cart: CartSession = JSON.parse(cartJSON);
  //     console.log(cart);
  //   }
  //   this.cartService.getCartSession().subscribe((res: any) => {
  //     console.log(res);
  //   });
  // }

  formatNumber(price: number) {
    return this.formatNumberWithCommas(price);
  }
}
