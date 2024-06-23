import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
  RouterEvent,
} from '@angular/router';
import { BreadcrumbService, Breadcrumb } from 'angular-crumbs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'frontend-app';
  loading = true;

  constructor(
    private router: Router,
    private titleService: Title,
    private breadcrumbService: BreadcrumbService
  ) {
    this.router.events.subscribe((e: any) => {
      this.navigationInterceptor(e);
    });
  }

  ngOnInit(): void {
    this.breadcrumbService.breadcrumbChanged.subscribe((crumbs) => {
      this.titleService.setTitle(this.createTitle(crumbs));
    });
  }

  navigationInterceptor(event: RouterEvent): void {
    if (event instanceof NavigationStart) {
      this.loading = true;
    }

    if (event instanceof NavigationEnd) {
      setTimeout(() => {
        this.loading = false;
      }, 300);
    }

    if (event instanceof NavigationCancel) {
      setTimeout(() => {
        this.loading = false;
      }, 300);
    }
    if (event instanceof NavigationError) {
      setTimeout(() => {
        this.loading = false;
      }, 300);
    }
    
  }

  private createTitle(routesCollection: Breadcrumb[]) {
    const title = 'Angular Shop';
    const titles = routesCollection.filter((route) => route.displayName);

    if (!titles.length) {
      return title;
    }

    const routeTitle = this.titlesToString(titles);
    return `${routeTitle} ${title}`;
  }

  private titlesToString(titles: any) {
    return titles.reduce((prev: any, curr: any) => {
      return `${curr.displayName} - ${prev}`;
    }, '');
  }
}
