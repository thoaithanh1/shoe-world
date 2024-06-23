package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SizeShowClientDto {

    private Long sizeId;

    private String sizeName;

    private Integer quantity;

    private BigDecimal price;
}
