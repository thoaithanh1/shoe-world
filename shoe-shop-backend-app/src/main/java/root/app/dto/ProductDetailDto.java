package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailDto {

    private Integer id;

    private String mainImage;

    private String productName;

    private String quantity;

    private BigDecimal price;

    private Integer gender;

    private String colorName;

    private String sizeName;

    private String brandName;

    private String categoryName;

    private Boolean status;
}
