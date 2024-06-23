package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto {

    private Long userId;

    private Long productDetailId;

    private Integer quantity;

    private BigDecimal totalPrice;
}
