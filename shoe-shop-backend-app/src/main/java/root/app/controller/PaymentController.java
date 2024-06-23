package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.dto.OrderPaymentDto;
import root.app.dto.PaymentDto;
import root.app.service.PaymentService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<PaymentDto> showCartInPayment(@PathVariable("userId") Long userId) {
        PaymentDto paymentDto = paymentService.showInfoCartInPayment(userId);
        return ResponseEntity.ok(paymentDto);
    }

    @PostMapping("/by-cash")
    public ResponseEntity<?> paymentOrderByPayAfterReceive(@RequestBody OrderPaymentDto orderPaymentDto) {
        Boolean isSuccess = paymentService.paymentOrderByPayAfterReceive(orderPaymentDto);
        Map<String, Integer> result = new HashMap<>();
        result.put("status", isSuccess ? 1:0);
        System.out.println("Status: " + isSuccess);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vnpay")
    public ResponseEntity<?> paymentOrderByVnPay(@RequestParam("totalPrice")BigDecimal totalPrice,
                                                 @RequestParam("note") String note) {
        try {
            String urlVnPay = paymentService.redirectPagePaymentVnPay(totalPrice, note);
            Map<String, String> result = new HashMap<>();
            result.put("url", urlVnPay);
            return ResponseEntity.ok(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/vnpay/response")
    public ResponseEntity<?> getResponseInvoiceAfterPaymentVnpay(@RequestParam("vnp_Amount") String totalPrice,
                                                                 @RequestParam("vnp_CardType") String paymentType,
                                                                 @RequestParam("vnp_TxnRef") String codeOrder,
                                                                 @RequestParam("vnp_OrderInfo") String note,
                                                                 @RequestParam("user_id") Long userId) {

        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(totalPrice)).divide(BigDecimal.valueOf(100));

        Boolean isSuccess = paymentService.paymentOrderByVnPay(amount, paymentType, codeOrder, note, userId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("status", isSuccess);
        return ResponseEntity.ok(result);
    }
}
