import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

export function formatNumberWithCommas() {
  return (inputNumber: number) => {
    const numberStr = inputNumber + '';
    const parts = numberStr.split('.');
    const integerPart = parts[0];
    const decimalPart = parts[1] ? '.' + parts[1] : '';

    const integerWithCommas = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    return integerWithCommas + decimalPart;
  };
}

export function scrollToTopSmoothly() {
  return (endNumber: number) => {
    const duration = 500;
    const start = window.scrollY;
    const end = endNumber;
    const startTime = performance.now();
  
    function step(time: number) {
      const currentTime = time - startTime;
      const progress = Math.min(currentTime / duration, 1);
      window.scrollTo(0, start + (end - start) * progress);
  
      if (progress < 1) {
        requestAnimationFrame(step);
      }
    }
    requestAnimationFrame(step);
  }
}

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
  ],
  providers: [
    {provide: 'formatNumberWithCommas', useFactory: formatNumberWithCommas},
    {provide: 'scrollToTopSmoothly', useFactory: scrollToTopSmoothly},
  ]
})
export class GeneralUseFunctionModule {}
