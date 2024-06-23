import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageStatusCashPaymentComponent } from './page-status-cash-payment.component';

describe('PageStatusCashPaymentComponent', () => {
  let component: PageStatusCashPaymentComponent;
  let fixture: ComponentFixture<PageStatusCashPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageStatusCashPaymentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageStatusCashPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
