package root.app.service;

import root.app.dto.OrderPaymentDto;
import root.app.dto.PaymentDto;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface PaymentService {

    PaymentDto showInfoCartInPayment(Long userId);

    Boolean paymentOrderByPayAfterReceive(OrderPaymentDto orderPaymentDto);

    Boolean paymentOrderByVnPay(BigDecimal totalPrice,
                                String paymentType,
                                String codeOrder,
                                String note,
                                Long userId);

    String redirectPagePaymentVnPay(BigDecimal totalPrice, String note) throws UnsupportedEncodingException;
}
