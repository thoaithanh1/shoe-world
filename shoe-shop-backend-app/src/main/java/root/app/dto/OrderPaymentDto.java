package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderPaymentDto {

    private BigDecimal shippingFee;

    private BigDecimal totalPrice;

    private String note;

    private Integer paymentMethodId;

    private Long userId;

    private Long promotionId;

    private List<CartDetailDto> cartDetailDtos;
}
