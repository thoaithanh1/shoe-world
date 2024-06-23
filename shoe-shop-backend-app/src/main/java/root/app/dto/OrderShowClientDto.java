package root.app.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderShowClientDto {

    private Long id;

    private String code;

    private BigDecimal total;

    private BigDecimal shippingFee;

    private Integer status;

    private Date createdDate;

    private Date cancelledDate;

    private Date completedDate;

    private List<OrderDetailDto> orderDetails;

    public OrderShowClientDto(Long id, String code, BigDecimal total, BigDecimal shippingFee, Integer status, Date createdDate, Date cancelledDate, Date completedDate) {
        this.id = id;
        this.code = code;
        this.total = total;
        this.shippingFee = shippingFee;
        this.status = status;
        this.createdDate = createdDate;
        this.cancelledDate = cancelledDate;
        this.completedDate = completedDate;
    }
}
