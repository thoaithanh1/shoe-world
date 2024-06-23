package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
    private String id;
    private String name;
    private String material;
    private String description;
    private String brandName;
    private String categoryName;
    private Boolean status;
}
