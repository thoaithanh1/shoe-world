package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.app.entity.ProductDetail;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDetailDto {

    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private ProductDetail productDetail;
}
