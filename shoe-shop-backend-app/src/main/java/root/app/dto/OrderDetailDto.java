package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDto {

    private Long orderId;

    private String name;

    private Long productDetailId;

    private String image;

    private Integer quantity;

    private BigDecimal price;

    private String size;

    private String color;
}
