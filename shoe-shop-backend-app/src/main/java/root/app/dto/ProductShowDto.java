package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductShowDto {

    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private BigDecimal discount;
    private Date createdDate;
    private Boolean status;
}
