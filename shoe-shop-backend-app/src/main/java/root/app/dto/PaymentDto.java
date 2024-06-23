package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.app.entity.Cart;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {

    private Cart carts;

    private List<AddressDto> addresses;

    private List<CartDetailDto> cartDetails;
}
