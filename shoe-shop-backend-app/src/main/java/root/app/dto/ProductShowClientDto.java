package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductShowClientDto {

    private Long id;

    private String image;

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private Long productId;

    private Long colorId;

    private Long sizeId;

    private String sizeName;

    private String description;

    private Boolean status;

    private List<SizeShowClientDto> productDetails;

    private List<ImagesShowClientDto> productImages;

    public ProductShowClientDto(Long id, String image, String name, Integer quantity, BigDecimal price, Long productId, Long colorId, Long sizeId, String sizeName, String description, Boolean status) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.description = description;
        this.status = status;
    }
}
