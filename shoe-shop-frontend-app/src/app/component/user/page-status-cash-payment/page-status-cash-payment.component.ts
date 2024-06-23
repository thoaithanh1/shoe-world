import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-page-status-cash-payment',
  templateUrl: './page-status-cash-payment.component.html',
  styleUrls: ['./page-status-cash-payment.component.css']
})
export class PageStatusCashPaymentComponent implements OnInit{

  successStatus: boolean = true;
  failStatus: boolean = true;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const status = params.get('status');
      if(status === "success") {
        this.successStatus = true;
        this.failStatus = false;
      } else if(status === "fail") {
        this.successStatus = false;
        this.failStatus = true;
      }
    });
  }
}
