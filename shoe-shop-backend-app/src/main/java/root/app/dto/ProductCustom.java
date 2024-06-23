package root.app.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface ProductCustom {
    Long getProductId();
    String getProductName();
    String getProductImage();
    BigDecimal getPrice();
    BigDecimal getDiscount();
    Date getCreatedDate();
    Boolean getStatus();
}
